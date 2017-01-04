# -*- coding: utf-8 -*-
"""
Created on Friday Dec 2 13:17:56 2016

@author: Ashtyn Warner
"""
from manager import DataManager
from disease import Disease
from util import load_object, save_object
import os, sys

def map_new_fields(dm, disease):
	unmapped_lines = disease.unmapped_lines()
	print '\n\n'
	#for key, value in unmapped_lines.iteritems():
	while len(unmapped_lines) > 0:
		key, value = unmapped_lines.popitem()
		selected_group = ''
		print 'Now matching line ' + str(key) + "; Name: " + str(value[0]) + '\nField Description: ' + str(value[1]).replace('\n','')
		matches = disease.search_mapped_lines(key)
		print 'Found potential matches:'
		counter = 1
		for match in matches:
			print str(counter) + ': '
			print '\tGroup Name: ' + str(match[2]).replace('\n','')
			print '\tGroup Description: ' + str(match[3]).replace('\n','')
			print '\tCorresponding FHIR Resource: ' + str(match[4]).replace('\n','')
			print '\tMatched Label Name: ' + str(match[0]).replace('\n','')
			print '\tDescription: ' + str(match[1]).replace('\n','')
			counter = counter + 1

		while True:
			matching = raw_input('Enter the line number if one of the located resources above matches this record, or \'none\' or \'n\' to add your own.\n')
			if matching[0].lower() == 'n':
				other_matching = raw_input('Does this match any other existing group you know of? Type \'y\' or \'n\'\n')
				if other_matching[0].lower() == 'y':
					group_name = raw_input('Enter the name of the group this record belongs to.\n')
					record = dm.search_existing_concepts(group_name)
					if record is None:
						search_or_add = raw_input('This does not match any existing groups. Type \'a\' or \'add\' to add this concept, or \'l\' or \'list\' to see all existing concepts.\n')
						if search_or_add[0].lower() == 'l':
							print dm.get_existing_concepts()
							group_name = raw_input('Enter the name of the group this record belongs to from the list above.\n')
							record = dm.search_existing_concepts(group_name)

						elif search_or_add[0].lower() == 'a':
							new_description = raw_input('Please enter a description for this resource.\n')
							new_resource = raw_input('Enter the FHIR Resource that this should map to.\n')
							new_record = ['', '', group_name, new_description, new_resource]
							selected_group = new_record
							add_existing_record(key, new_record, disease)
							break
						else:
							print 'Sorry, that\'s not a valid response.'

					if record is not None:
						print 'Found this record:'
						print '\tGroup Name: ' + str(record[2]).replace('\n','')
						print '\tGroup Description: ' + str(record[3]).replace('\n','')
						print '\tCorresponding FHIR Resource: ' + str(record[4]).replace('\n','')

						is_match = raw_input('Is this the resource you want to use?Type \'y\' or \'n\'\n')
						if is_match[0].lower() == 'y':
							selected_group = record
							add_existing_record(key, record, disease)
							break
						else:
							other_matching = 'n'

				elif other_matching[0].lower() == 'n':
					print 'Adding a new group for this field.'
					group_name = raw_input('Enter the group name.\n')
					group_description = raw_input('Enter the group description.\n')
					group_resource = raw_input('Enter the group resource.\n')
					new_record = ['', '', group_name, group_description, group_resource]
					selected_group = new_record
					add_existing_record(key, new_record, disease)
					break

				else:
					print 'Sorry, that\'s not a valid response'

			#the group already exists
			elif matching in ['1', '2', '3', '4', '5']:
				chosen_match = matches[int(matching)-1]
				selected_group = chosen_match
				add_existing_record(key, chosen_match, disease)
				break

			else:
				print 'Sorry, that\'s not a valid response.\n'
		
		if len(unmapped_lines )> 10:
			print '\n\nFound these other fields that might be a match for this grouping as well.'
			similar_fields = disease.search_unmapped_lines(key, 10)

			counter = 1
			for field in similar_fields:
				print str(counter) + ': '
				print '\tLabel Name: ' + str(field[1]).replace('\n','')
				print '\tDescription: ' + str(field[2]).replace('\n','')
				counter = counter + 1

			add_more = raw_input('If you want to add the grouping to any of the records above, please enter their line numbers below separated by comma. If you don\'t want to add this class to any of these, type \'n\' or \'next\'\n')
			line_nums = add_more.split(',')
			for i in line_nums:
				if i in ['1','2','3','4','5','6','7','8','9','10']:
					add_existing_record(similar_fields[int(i)-1][0], selected_group, disease)
					del unmapped_lines[similar_fields[int(i)-1][0]]


		print '################################################################################################################\n\n'


def add_existing_record(key, line, disease):
	if line[4].lower() == 'questionnaire':
		disease.add_question_concept(line[2], line[3], warn=False)
		disease.map_line_to_questionnaire(key, line[2])
	else:
		#maps 'none' to 'Basic' - a resource for things not supported in FHIR. For now, this acts as a placeholder for core data types, as leaving
		#them unmapped causes problems when finding matches but they don't need to be in queries since the information is redundant.
		if line[4].lower() == 'none' or line[4].lower() == 'na' or line[4].lower() == 'n/a':
			line[4] = 'Basic'
		disease.add_fhir_group(line[2], line[3], line[4], warn=False)
		disease.map_line_to_fhir_group(key, line[2])
		if line[4] != 'Basic':
			has_criteria = raw_input('Is there any FHIR search criteria associated with this resource? Type \'y\' or \'n\'\n')
			if has_criteria[0].lower() == 'y':
				while True:
					user_criteria_name = raw_input('Please specify the name of the search field\n')
					user_criteria_value = raw_input('Please specify the value to search for\n')
					user_criteria = {user_criteria_name: user_criteria_value,}
					if user_criteria_name != '':
						disease.add_criteria_to_fhir_group(line[2], user_criteria)
					more_criteria = raw_input('Do you have any more criteria to add? Type \'y\' or \'n\'\n')
					if more_criteria[0].lower() == 'n':
						break
			elif has_criteria[0].lower() != 'n':
				print 'Sorry, that\'s not a valid response.\n'


def run_ui():
	dm = DataManager()

	disease_name = raw_input('Name of the disease spreadsheet you\'d like to process?\n')
	
	file_path = "../data/" + disease_name + ".csv"
	# Check if that file exists
	if not os.path.exists(file_path) or not os.path.isfile(file_path):

		print '\nSorry, a spreadsheet for that disease does not exist in the data directory.'
		disease_list = [s[:-4] for s in os.listdir("../data/")]

		while True:

			nextcmd = raw_input('You can type another disease name, \'l\' or \'list\' to see all diseases to choose from, or \'e\' or \'exit\' to quit.\n')

			if nextcmd.lower() == 'l' or nextcmd.lower() == 'list':
				print '\n'
				for i in disease_list:
					if i == disease_list[len(disease_list)-1]:
						sys.stdout.write(i)
					else:
						sys.stdout.write(i + ', ')
				print '\n'

			elif nextcmd.lower() == 'e' or nextcmd.lower() == 'exit':
				return

			else:
				file_path = "../data/" + nextcmd + ".csv"
				# Check if that file exists
				if os.path.exists(file_path) or os.path.isfile(file_path):
					disease_name = nextcmd
					break
				else:
					print '\nSorry, a spreadsheet for that disease does not exist in the data directory.'


	disease = dm.diseases[disease_name]

	if len(disease.unmapped_lines()) == 0:
		while True:
			start_over = raw_input('There are no unmapped fields in this sheet. Type \'S\' or \'start over\' to delete all existing mappings and start over. To edit mappings, please use the interactive Python console.\n')
			if start_over[0].lower() == 'e':
				edit_existing_records(dm, disease)
				break

			elif start_over[0].lower() == 's':
				for key, value in disease.mapped_lines().iteritems():
					disease.unmap_line(key)

				map_new_fields(dm, disease)
				break
			else:
				print 'Sorry, that\'s not a known command.'


	elif len(disease.mapped_lines()) != 0:
		while True:
			start_over = raw_input('This spreadsheet is already partially mapped. Type \'A\' or \'add\' to complete only the missing mappings, or \'S\' or \'start over\' to start over.\n')
			if start_over[0].lower() == 'a':
				map_new_fields(dm, disease)
				break

			elif start_over[0].lower() == 's':
				for key, value in disease.mapped_lines().iteritems():
					disease.unmap_line(key)

				map_new_fields(dm, disease)
				break
			else:
				print 'Sorry, that\'s not a known command.'


	else:
		map_new_fields(dm, disease)


	print 'Mapping is done. Saving your spreadsheet with new mappings to the data folder. '
	disease.to_csv()


if __name__=='__main__':
	run_ui()

    