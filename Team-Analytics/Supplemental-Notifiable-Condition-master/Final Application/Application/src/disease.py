# -*- coding: utf-8 -*-
"""
Manages the lines and groupings for a disease.

@author: Kirk, Ashtyn
"""
from querygroup import QueryGroup
from questionnaire import Questionnaire
import pandas as pd
from util import sort_tuples

class Disease(object):
    def __init__(self, df, disease_name, data_manager, 
                 data_path='../data/', pickle_path='../pickle/'):
        """
        Initializes a disease notifiable fields object with a pandas DataFrame.
        
        df requires at least 3 columns and a range index.
        optional columns are "Group", "Group Description", "Resource" (expects all 3)
        
        Rebuilds the groupings from group columns, if available.
        """
        self.data_path = data_path
        self.pickle_path = pickle_path
        self.disease_name = disease_name
        self.df = df
        self.index = df.index
        self.fhir_groups = {}
        self.data_manager = data_manager

        group_col = "Group"
        self.group_columns = [group_col, "Group Description", "Resource"]
        
        cols = self.df.columns
        
        # Generate a dictionary of {line#: (label, description)}
        self.lines = {i: tuple(df.loc[i, cols[:2]].values) for i in df.index}                
                          
        # Generate default Questionnaire
        self.questionnaire = Questionnaire(self.lines, self.disease_name)
        
        # if groupings have already been calculated, add them
        # The reverse process is done in save_disease
        if group_col in cols:
            for i in df.index:
                group_name, group_desc, resource = df.loc[i, cols[3:]].values
                if not pd.isnull(group_name) and len(str(group_name).strip())>0:
                    if resource.lower()=='questionnaire':
                        self.add_question_concept(group_name, group_desc,warn=False)
                        self.map_line_to_questionnaire(i, group_name)
                    else:
                        self.add_fhir_group(group_name, group_desc, resource,warn=False)
                        self.map_line_to_fhir_group(i, group_name)

        #if the columns don't already exist, add empty ones to the dataframe
        for column in self.group_columns:
            self.df[column] = None
    
    def __getitem__(self, i):
        """ Get's the i-th item in the dataframe (includes group info). """
        if i in self.lines:
            return self.lines[i]
        return None
        
    def __contains__(self, i):
        return i in self.lines

    def add_fhir_group(self, group_name, group_description, fhir_resource, warn=True):
        if group_name not in self.fhir_groups:
            group = QueryGroup(self.disease_name, fhir_resource, group_name, 
                               group_description, self.pickle_path)
            self.fhir_groups[group_name] = group
        elif warn:
            print("Group '{}' already exists".format(group_name))
    
    def map_line_to_fhir_group(self, line_number, group_name):
        """
        Map a line to a fhir resource by giving the line # and group name.
        To see what groups are available, check out the fhir_gorups variable.
        To create a new group, use add_fhir_group.
        
        Adds the mapping to 
        """
        if group_name not in self.fhir_groups:
            print("Group {} not found in fhir_groups. Try adding the group first.".format(group_name))
            return
            
        self.unmap_line(line_number)
        grp = self.fhir_groups[group_name]
        label, description = self.lines[line_number]
        grp.map_line(line_number, label, description)
    
    def add_criteria_to_fhir_group(self, group_name, criteria_dict):
        """
        Given a group name, updates the search query criteria.
        The criteria dict are pairs of field:value that specify a single query.
        """
        if group_name not in self.fhir_groups:
            print("Could not add criteria to group: {}. Does not exist".format(group_name))
        group = self.fhir_groups[group_name]
        group.add_criteria(criteria_dict)
        
    def add_question_concept(self, concept, description, warn=True):
        if concept not in self.questionnaire:
            self.questionnaire.add_group(concept, description)
        elif warn:
            print("Questionnaire concept group '{}' already exists".format(concept))
        
    def map_line_to_questionnaire(self, line_number, concept):
        """
        Map a line to a questionnaire group by giving the line # and group name.
        To see what groups are available, check out the questionnaire variable.
        To create a new group, use add_question_concept.
        """
        if concept not in self.questionnaire:
            print("Group {} not found in questionnaire. Try adding the question concept first.".format(concept))
            return
        self.unmap_line(line_number)
        self.questionnaire.map_line(concept, line_number)
    
    def unmap_line(self, line_number):
        """
        Removes a line from any existing mapping.
        """
        for concept, group in self.fhir_groups.items():
            group.unmap_line(line_number)
        self.questionnaire.unmap_line(line_number)
        
    def unmapped_lines(self):
        """
        Returns a dict of:
        line#: (Label, Description)
        For all the lines in the disease that are still in the default
        Questionnaire group.
        """
        line_numbers = self.questionnaire.unmapped_lines()
        for grp_name, grp in self.fhir_groups.items():
            line_numbers.extend(grp.lines.keys())
        
        lines = {i: v for i,v in self.lines.items() if i not in line_numbers}
        return lines
        
    def mapped_lines(self):
        """
        Returns a dictionary of {line#: (group, description, resource)}
        for lines that have been mapped to fhir groups or question concepts.
        """
        lines = self.questionnaire.mapped_lines()
        for grp_name, grp in self.fhir_groups.items():
            for line in grp.lines:
                lines[line] = (grp.name, grp.description, grp.resource_name)
        # Add the line's label and description to each tuple
        for num, line in lines.items():
            lines[num] = self.lines[num] + line

        #print("Output columns are (Label, Description, {}, {}, {})".format(*self.group_columns))
        return lines
    
    def search_mapped_lines(self, line_number):
        label, description = self.lines[line_number][:2]
        #print("Searching for line # {}: {}; {}.\n".format(line_number, label, description))
        return self.data_manager.search_mapped_lines(str(label) + " " + str(description))
    
    def search_unmapped_lines(self, line_number, n=10):
        """ Finds unmapped lines in this disease to facilitate grouping. """
        label, description = self.lines[line_number][:2]
        print("Searching for line # {}: {}; {}.\n".format(line_number, label, description))
        lines = [(i,)+v for i,v in self.unmapped_lines().items()]
        sort_tuples(lines, description, 2)
        return lines[1:n+1]  # The top match will always be the input line
        
    def refresh_df(self):
        """
        Uses the information in the questionnaire groups to update the DataFrame.
        """
        # Don't refresh if no mappings have been created
        if not all(col in self.df.columns for col in self.group_columns):
            return
        # delete info in the csv    
        self.df.loc[:, self.group_columns] = pd.np.nan
        # Add the latest groupings back in
        questionnaire_lines = self.questionnaire.mapped_lines()
        
        fhir_query_lines = {}
        for grp_name, grp in self.fhir_groups.items():
            line_nums = grp.lines.keys()
            fhir_query_lines.update({i:(grp.name, grp.description, grp.resource_name) for i in line_nums})
            
        for i in questionnaire_lines:
            line = questionnaire_lines[i]
            self.df.loc[i, self.group_columns] = line
        for i in fhir_query_lines:
            self.df.loc[i, self.group_columns] = fhir_query_lines[i]
                    
    def to_csv(self, path=None):
        """
        Saves the most updated DataFrame (including group mappings) to csv.
        Overwrites the old csv in the data path if no path is specified.
        Also pickles the criteria in the FHIR query groups.
        """
        if path is None:
            path = self.data_path
            
        self.refresh_df()
        if self.group_columns[0] in self.df.columns:
            self.df.to_csv(path + self.disease_name + '.csv', index=False)
        
        for group_name, group in self.fhir_groups.items():
            group.pickle()
            
    def as_json(self):
        print("JSON representations of the FHIR query groups:\n")

        for group_name, group in self.fhir_groups.items():
            print("{}:".format(group_name))
            print(group.as_json())
            print("\n")
        
        print("Questionnaire JSON:\n")
        print(self.questionnaire.as_json())