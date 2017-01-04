#!/usr/bin/python

# FHIRFighters - GTCS6440
#	Peter Graening
#	Tony Potter
#	Shashindra Pradhan
#	Ali Shaikh
#	Radha Venkataraman

from DBAccessor import DBAccess
import createPatient as cp
import sys
import requests
import json
import networkConstants as nc

def printsyntax(args):
	# if this is called, we got incorrect syntax. flip the table.
	print (""" Syntax:
				python doh2fhir.py  
				python doh2fhir.py  [deathmm/dd/yyyy] 
				python doh2fhir.py  [startmm/dd/yyyy] [endmm/dd/yyyy]
				python doh2fhir.py  [lastname]
				python doh2fhir.py  [firstname] [lastname]
				python doh2fhir.py  [dohmpinumber] """)
	raise SyntaxError("Invalid args: %s" % args)

def processPatient(pat):
	# process a patient, entering them to the FHIR server indicated in networkConstants.py
	
	url = nc.fhirurl + ':' + nc.fhiraccessport + '/' +nc.fhirpath

	datum = json.dumps(pat.as_json())	# format JSON with double-quotes instead of single quotes

	header = {'Content-Type':'application/json+fhir'}
	response = requests.post(url, headers=header, data=datum, verify=False)

	return response

def processRecords(fields,records):
	# Process all records
	if (len(records) < 1):
		print ("No records to process.")
	else:
		for i in range(len(records)):
			pat = cp.createPatient(fields,records[i])
			result = processPatient(pat)
			
			if (result.status_code != 201):
				print ("PROBLEM: COULD NOT PROCESS PATIENT %d" % (i+1))
				f = file('error.html','w')
				f.write(result.text)
				print ("Error saved at 'error.html'")
			else:
				print ("processed patient %d of %d: %s, %s" % (i+1,len(records),pat.name[0].family[0],pat.name[0].given[0]))

def processDeathDate(fields,date):
	#given a date, process people who died on that date.
	db = DBAccess()
	records = db.getRecordsByDeathDate(date)
	processRecords(fields,records)

def processDOHMPI(fields,mpi):
	#given a dohmpi number, process that person.
	db = DBAccess()
	records = db.getRecordsByMPI(mpi)
	processRecords(fields,records)

def processLastName(fields,lname):
	# given a last name, process all people with that last name.
	db = DBAccess()
	records = db.getRecordsByName("%",lname.upper())
	processRecords(fields,records)

def processName(fields,fname, lname):
	# given a first and last name, process people with that first name and last name
	db = DBAccess()
	records = db.getRecordsByName(fname.upper(),lname.upper())
	processRecords(fields,records)
	
def processRange(fields,start, end):
	# given two dates, process all dates between them.
	db = DBAccess()
	records = db.getRecordsByDeathRange(start,end)
	processRecords(fields,records)
	
def stripdate(date):
	# If date includes leading 0's, strip them, then return the stripped date
	return date
	slash = date.index("/")
	mo = date[0:slash]
	if (mo[0] == "0"):
		mo = mo[1]
	slash2 = date[slash+1:].index("/")
	dy = date[slash+1:slash+1+slash2]
	if (dy[0] == "0"):
		dy = dy[1]
	yr = date[-4:]
	date = mo + "/" + dy + "/" + yr
	print date
	return date
	
def paddate(date):
	# If date is missing leading 0's, put them in for month and date, then return the padded date
	slash = date.index("/")
	mo = date[0:slash]
	if (mo[0] != "0"):
		mo = "0"+mo[0]
	slash2 = date[slash+1:].index("/")
	dy = date[slash+1:slash+1+slash2]
	if (dy[0] != "0"):
		dy = "0"+dy[0]
	yr = date[-4:]
	date = mo + "/" + dy + "/" + yr
	return date

def main():
	# Set up command-line arguments.
	args = sys.argv
	arg1 = ""
	arg2 = ""
	db = DBAccess()
	fields = db.getDohmpiFieldNames()
	if (len(args) > 3): # too many arguments, pitch a fit.
		printsyntax(args)
	if (len(args) >= 2):
		arg1 = sys.argv[1]
	if (len(args) == 3):
		arg2 = sys.argv[2]
	
	# Run tasks based on format of arguments.
	if (len(args) == 1): # no arguments, process current date
		processDeathDate(fields,db.getCurrentDate())
		
	elif (len(args) == 2):	# one argument
		# check if date or name or id
		if (db.isdate(arg1)): # is it a date?
			arg1 = paddate(arg1)
			processDeathDate(fields,arg1)
			
		elif ("-" in arg1): # hyphen may mean provider license number
			index = arg1.index("-") 
			if (arg1[0:index].isdigit() and arg1[index+1:].isdigit()):
				processDOHMPI(fields,arg1)
				
			else:			# must be a hyphenated last name.
				processLastName(fields,arg1)
				
		else:				# must be an unhyphenated last name.
			processLastName(fields,arg1)
			
	elif (len(args) == 3): # two arguments, date range or first and last name
		if (db.isdate(arg1) and db.isdate(arg2)):
			arg1 = paddate(arg1)
			arg2 = paddate(arg2)
			processRange(fields,arg1,arg2)	# date range
			
		elif (not db.isdate(arg1) and not db.isdate(arg2)):
			processName(fields,arg1, arg2)	# first and last name
			
		else:
			printsyntax(args) # no clue, remind user of command line options.
		
if __name__ == "__main__":
	main()
