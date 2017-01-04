# -*- coding: utf-8 -*-
"""
generates a questionnaire

@author: Kirk
"""

from datetime import date
import json

class Questionnaire(object):
    def __init__(self, lines, disease_name):
        """ 
        lines is a dict of {line# : description} pairs. 
        They aren't used until added to a group (add_line method)
        """
        self.lines = lines
        self.disease_name = disease_name
        self.mapped_line_numbers = {} # {#: group_name}
        self.groups = {}
        
    def add_group(self, concept, description):
        """ Adds a new questionnaire group for a single concept. """
        self.groups[concept] = Group(concept, description)
        
    def map_line(self, concept, line_num):
        """
        Maps a line to a question group.
        If the line is already mapped somewhere, it removes the line from its
        current group first.
        """
        if concept not in self.groups:
            print("Cannot add to concept: {}.\nConcept group does not exist.".format(concept))
            return
        if line_num not in self.lines:
            print("Cannot map line # {}. Does not exist.".format(line_num))
            return
            
        self.unmap_line(line_num)

        label, description = self.lines[line_num]
        grp = self.groups[concept]
        grp.add_line(line_num, label, description)
        self.mapped_line_numbers[line_num] = concept
    
    def unmap_line(self, line_num):
        """
        Unmaps a line from a question group.
        """
        if line_num in self.mapped_line_numbers:
            concept = self.mapped_line_numbers[line_num]
            del self.mapped_line_numbers[line_num]
            if concept in self.groups:
                self.groups[concept].remove_line(line_num)

    def mapped_lines(self):
        """ Returns a dict of 
        line#: (grp concept, grp description)
        """
        grouped_lines = {}
        for group_name, grp in self.groups.items():
            for line_number in grp.lines:
                grouped_lines[line_number] = (grp.concept, grp.description, 'Questionnaire')
        return grouped_lines
        
    def unmapped_lines(self):
        line_numbers = []
        for grp_name, grp in self.groups.items():
            line_numbers.extend(grp.lines.keys()) 
        return line_numbers
    
    def __contains__(self, concept):
        return concept in self.groups
    
    def __getitem__(self, concept):
        return self.groups[concept]
        
    def as_json(self):
        """
        Returns the Questionnaire as json.
        Note that "group" and "question" keys are duplicated manually when necessary.
        
        TODO: The coding for each question would need to be specified in production.
        The data for code types would be added to the Group when mappings are added.
        Then they could be added accurately below
        This needs some work, but the general function structure should be the same
        once the finer points like coding and concepts are sorted out.
        
        FHIR uses coding and codableConcepts, which would need to be figured 
        out during the mapping stage in order for the JSON to actually work 
        with FHIR.  Things like uri's and valid data types aren't implemented, 
        but they would be straightforward to implement into the functions for 
        adding mappings, and then insert that into the loop that produces JSON 
        for groups and their questions. Figuring out the specifics for that 
        seems like a job for subject matter experts, so I'm done.
        """
        # contained: https://www.hl7.org/fhir/references.html#contained
        # seems to also go here, but it doesn't seem required
        header_dict = {"resourceType": "Questionnaire",
                     "id": self.disease_name,
                     "text": {"status":"generated",
                              "div": "xhtml goes here, but Ill leave that to the experts"},
                     "status":"draft",
                     "date":str(date.today()),
                     "subjectType": ["Patient"]}
        # Groups need to be joined manually, same reason as question
        json_string = json.dumps(header_dict)[:-1] + ", "
        
        groups_json_string = '"group": '
        group_strings = []
        for i, concept in enumerate(self.groups.keys()):
            group = self.groups[concept]
            group_dict = {"linkID":i,
                          "title":concept,
                          "text":group.description,
                          "repeats":False}
            grp_str = json.dumps(group_dict)[:-1] + ", "
            # Each question just has the key "question" under the group
            # Since they would overwrite each other in a Python dict,
            # They json strings need to be joined manually
            question_strings = []
            for line_num, line in group.lines.items():
                label, description = line
                q_dict = {"linkID":'.'.join([str(i),str(line_num)]), #this seems like a reasonable id
                          "text":description,
                          "type":"code"}
                question_strings.append(json.dumps(q_dict))
            grp_str += '"question": ' + ', "question": '.join(question_strings) + "}"
            group_strings.append(grp_str)
            
        groups_json_string += ', "group": '.join(group_strings)
        json_string += groups_json_string + "}"
        return json_string
        
class Group(object):
    def __init__(self, concept, description):
        self.concept = concept
        self.description = description
        self.lines = {}
        
    def add_line(self, line_number, label, description):
        self.lines[line_number] = (label, description)
        
    def remove_line(self, line_number):
        if line_number in self.lines:
            del self.lines[line_number]
    
    