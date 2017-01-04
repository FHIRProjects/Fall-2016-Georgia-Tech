# -*- coding: utf-8 -*-
"""
Created on Sun Oct 16 15:57:05 2016

DataManager class for opening and managing csv files made by DataConverter.

TODO: Every time a search is run, the mappings are re-retrieved from the
the disease objects and their group containers.  This is inefficient,
but not so much that it's a problem right now.  Should keep a rolling record.

@author: Kirk, Ashtyn
"""
import os
import pandas as pd
from util import sort_tuples
from disease import Disease
#from util import load_object, save_object

class DataManager(object):
    core_name = 'eICR Core'
    
    def __init__(self, data_path='../data/', disease_list=[]):
        """
        Initializes the object by loading all csv data in data_path.
        Can restrict the diseases loaded by passing disease_list.
        
        known_mappings keeps track of all the known {description: (group_name, group_description, fhir_resource)}
        """
        self.path = data_path
        self.dataframes_dict = self.load_data(data_path, disease_list)
        self.core_df = self.load_data(data_path, [self.core_name])[self.core_name]
        self.core_labels = set(self.core_df[self.core_df.columns[0]])
        self.diseases = {name: Disease(df, name, self) for name,df in self.dataframes_dict.items()}
        
    def search_mapped_lines(self, description, n=5):
        """
        Searches through all known mappings for similar descriptions,
        and returns the top n matches (default is 5) with their mappings.
        """
        # First, construct the list of mapped lines
        mapped_lines = []
        desc_col = 1 # column in each mapped_lines tuple containing the description
        for disease_name, disease in self.diseases.items():
            map_dict = disease.mapped_lines()
            mapped_lines.extend([v for i,v in map_dict.items()])

        sort_tuples(mapped_lines, description, desc_col)
        
        return mapped_lines[:n]

    def search_existing_concepts(self, name):
        """
        Searches through all known mappings for matching concept names,
        and returns only the top match, if it exists. 
        """
        # First, construct the list of mapped lines
        mapped_lines = []
        desc_col = 1 # column in each mapped_lines tuple containing the description
        for disease_name, disease in self.diseases.items():
            map_dict = disease.mapped_lines()
            mapped_lines.extend([v for i,v in map_dict.items()])

        for line in mapped_lines:
            if line[2].lower() == name.lower():
                return line

    def get_existing_concepts(self):
        """
        Returns all existing concept names 
        """
        # First, construct the list of mapped lines
        mapped_lines = []
        desc_col = 1 # column in each mapped_lines tuple containing the description
        for disease_name, disease in self.diseases.items():
            map_dict = disease.mapped_lines()
            mapped_lines.extend([v for i,v in map_dict.items()])

        concepts = []
        for line in mapped_lines:
            if line[2] not in concepts:
                concepts.append(line[2])

        return concepts

    def remove_core_from_all(self):
        """
        Removes core fields (leaving only Supplemental) from all dataframes.
        Modifies self.dataframes_dict
        """
        for disease in self.dataframes_dict:
            self.dataframes_dict[disease] = self.subtract_core[disease]
        
    def get_core_fields(self, disease):
        """
        Given a disease name (string), return the core fields from eICR Core.
        
        Returns a set of field labels (first dataframe column).
        Returns an empty set if the disease has not been loaded.
        
        TODO:
            Labels are not necessarily standardized across all spreadsheets
            May need to build a look-up table as discrepancies are found
        """
        if disease not in self.dataframes_dict:
            return set()
        
        df = self.dataframes_dict[disease]
        core_labels = self.core_labels.intersection(set(df[df.columns[0]]))
        
        return core_labels
        
    def subtract_core(self, disease):
        """
        Given a disease name (string), subtract the core fields from eICR Core.
        
        Returns a new dataframe with the core fields removed.
        Returns None if the disease has not been loaded.
        """
        core_fields = self.get_core_fields(disease)
        if core_fields is None:
            return None
        
        df = self.dataframes_dict[disease]
        drop_indices = [i for i in df.index if df.loc[i, df.columns[0]] in core_fields]
        return df.drop(drop_indices)
    
    def __getitem__(self, disease):
        """ Gets dataframe for the input disease. """
        return self.dataframes_dict[disease]

    @classmethod
    def load_data(cls, data_path, disease_list=[]):
        """
        Iterates over the *.csv files in the data_path folder.
        If disease_list contains names of diseases, it will load only those files.
        Otherwise, all csv files are loaded into pandas DataFrames.
        
        returns: dict of {disease_name: DataFrame}
        """
        # Remove the eICR Core if it's in data_list, 
        # since it will be imported separately
        if len(disease_list)>0 and cls.core_name in disease_list:
            disease_list.remove(cls.core_name)
            
        if len(disease_list)==0:
            disease_list = [s[:-4] for s in os.listdir(data_path)]
        disease_list = [s.lower() for s in disease_list]
                        
        csv_names = [name[:-4]
                     for name in os.listdir(data_path)
                     if name[-4:].lower()=='.csv' and name[:-4].lower() in disease_list]
        full_paths = [os.path.join(data_path,name+'.csv') for name in csv_names]

        dataframes = {name:pd.read_csv(path,index_col=None) 
                      for name, path in zip(csv_names, full_paths)}
        
        return dataframes
    
    def to_csv(self):
        for disease_name, disease in self.diseases.items():
            disease.to_csv(self.path)
    
if __name__=='__main__':
    dm = DataManager()
    diseases = dm.dataframes_dict.keys()
    #print('Loaded data for {} diseases:\n{}'.format(len(diseases),'\n'.join(diseases)))
    hep_core = dm.get_core_fields('Hepatitis')
    print('Core fields for Hepatitis are:\n{}\n'.format('\n'.join(hep_core)))
    hep_df = dm.subtract_core('Hepatitis')
    print('Hepatitis Supplemental Fields:')
    print(hep_df)
    
    