# -*- coding: utf-8 -*-
"""
Group class for managing sets of lines corresponding to a single FHIR query.
@author: Kirk
"""

import importlib
import json
from util import add_dicts, save_object, load_object

class QueryGroup(object):
    """ 
    Manages a group of lines from a disease notifiable fields
    in order to generate a FHIR query from a specific FHIR resource.
    
    Allows adding and removing lines, and defining a FHIR resource.
    User must add search criteria to generate a FHIR query.
     """
    def __init__(self, disease_name, fhir_resource, group_name, description,
                 pickle_path):
        self.disease = disease_name
        self.description = description
        self.name = group_name
        self.resource_name = fhir_resource
        self.set_resource(fhir_resource)
        self.lines = {}
        self.pickle_path = '{}{}-{}.p'.format(pickle_path,disease_name,fhir_resource)
        self.criteria = load_object(self.pickle_path, list)
        self.mappings = {}
        
    def __contains__(self, i):
        return i in self.lines
        
    def set_resource(self, fhir_model_name, fhir_resource_name=None):
        """
        Tries to import the resource from fhirclient.models
        Attempts equivalent of:
            from fhirclient.models.resource import Resource
        """
        module_name = 'fhirclient.models.{}'.format(fhir_model_name.lower())
        resource = None

        if fhir_resource_name is None:
            resource_name = fhir_model_name[0].upper() + fhir_model_name[1:]
        else:
            resource_name = fhir_resource_name

        try:
            model = importlib.import_module(module_name)
            resource = getattr(model, resource_name)
        except ImportError:
            print("Model not found: {}\n".format(module_name))
            print("Setting resource to None. Use set_resource to try again.")
        except AttributeError:
            print("Model {} was found, but attribute {} was not.".format(module_name, resource_name))
            print("Try using the optional argument fhir_resource_name in set_resource")
            
        self.resource = resource
        return resource
        
    def map_line(self, line_number, label, description, dtype=str, subfield=None):
        """ Adds a line to lines and mappings instance variables.
            Doesn't do anything with dtype yet.
            Subfield isn't 
        """
        self.lines[line_number] = (label, description, dtype)
        self.mappings[description] = subfield
    
    def unmap_line(self, line_number):
        """
        Removes a line from the lines and mappings instance variables.
        TODO: stop using hard-coded column number
        """
        if line_number in self.lines:
            _, description, _ = self.lines[line_number]
            del self.lines[line_number]
            
            if description in self.mappings:
                del self.mappings[description]

    def pop_line(self, line_number):
        """ Removes a line from the lines and mappings instance variables.
            Returns the label, description, and dtype. """
        if line_number not in self.lines:
            print('Line # {} not found. Available lines in this group ({}) are:'.format(line_number, self.name))
            self.print_lines()
            
        label, description, dtype = self.lines[line_number]
        
        del self.mappings[description]
        del self.lines[line_number]

        return (label, description, dtype)

    def print_lines(self):
        """ Prints the line numbers and descriptions in the group. """
        text = '\n'.join(['[{}] {}'.format(i, d) for i,d in self.get_descriptions()])
        print(text)
        
    def add_criteria(self, criteria_dict):
        """
        Add a search criteria to the group.
        The criteria dict are pairs of field:value that specify a single query.
        For example, for a liver protein test: {'code', '2885-2'}
        Used to generate FHIR queries.
        """
        # Search for an existing match
        if criteria_dict not in self.criteria:
            self.criteria.append(criteria_dict)
        
    def get_descriptions(self):
        """
        Returns the descriptions as a list of (#, description) pairs.
        """
        return [(lineno, x[1]) for lineno, x in self.lines.items()]
                
    def as_json(self):
        """
        Returns a string of valid JSON that contains all the information
        necessary to generate a query for this group of lines.
        """
        json_dict = {'id': self.name,
                     'description': self.description,
                     'disease': self.disease,
                     'resource': self.resource_name,
                     'criteria': self.criteria}
        return json.dumps(json_dict)
        
    def query_fhir(self, server, patientid):
        """
        Queries a fhir server for the records corresponding to the patient.
        """
        if self.resource is None:
            """ Cannot query fhir if resource is None.  Use set_resource """
            return None
        records = []
        for criteria in self.criteria:
            query = add_dicts({'patient':patientid}, criteria)
            search = self.resource.where(query)
            records.extend( search.perform_resources(server) )
        return records
        
    def pickle(self):
        save_object(self.pickle_path, self.criteria)