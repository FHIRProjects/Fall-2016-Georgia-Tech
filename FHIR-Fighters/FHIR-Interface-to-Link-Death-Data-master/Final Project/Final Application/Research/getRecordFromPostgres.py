#!/usr/bin/python

# To use:
# pip import psycopg2

import psycopg2
import sys
import datetime

# Database Constants
import networkConstants
host = networkConstants.host                      
dbname = networkConstants.dbname
user = networkConstants.user
password = networkConstants.password
table = networkConstants.table

# dohmpi postgres field names - for our use.
dohmpi = networkConstants.dohmpiFields


##############################################################################################################
# connection functions

def makeConn():
	# make a connection to the database, and return a connection object.
	conn_string = "host=%s dbname=%s user=%s password=%s" % (host,dbname,user,password)
	conn = psycopg2.connect(conn_string)
	return conn
	
def postgresGet(conn,sqlcommand,data=None):
	# given a connection conn and a command sqlcommand, return a cursor to all records retrieved by that command.
	cursor = conn.cursor()
	if (data):
		cursor.execute(sqlcommand,data)
	else:
		cursor.execute(sqlcommand)
	return cursor.fetchall()
	
##############################################################################################################
# utility functions	
def isdate(date):
	if (len(date) != 10):
		return False
	if (date[2] != '/' or date[5] != '/'):
		return False
	mo = int(date[0:2])
	dy = int(date[3:5])
	yr = int(date[6:])
	
	if (mo < 0 or mo > 12):
		print("badmonth")
		return False
	if (yr < 1900 or yr > 2100):
		print("badyear")
		return False
	if (dy < 0 or dy > 31):
		print("badday")
		return False
	if (dy == 31 and mo in [2,4,6,9,11]):
		print("badshortmonth")
		return False
	if (mo == 2 and dy == 30):
		print("badfeb")
		return False
	if (mo == 2 and dy == 29 and yr % 4 != 0):
		print("badleapfeb")
		return False
	return True
		
def checkargs(args):
	if (args[0] == 'all' and len(args) == 1):
		return True
	if ((args[0] == 'birth' or args[0] == 'death') and len(args) == 2 and isdate(args[1])):
		return True
	if (args[0] == 'range' and len(args) == 3 and isdate(args[1]) and isdate(args[2])):
		return True
	if (args[0] == 'mpi' and len(args) == 2 and int(args[1])):
		return True
	if (args[0] == 'name' and len(args) == 3):
		return True
	return False
	
def printsyntax():
	print("Syntax:")
	print("  python getRecordFromPostgres.py all")
	print("  python getRecordFromPostgres.py birth 01/01/1950")
	print("  python getRecordFromPostgres.py death 02/02/2010")
	print("  python getRecordFromPostgres.py range 03/03/2005 04/04/2005")
	print("  python getRecordFromPostgres.py mpi 123456789")
	print("  python getRecordFromPostgres.py name Fred Flintstone")

def queryFromGUI(conn,choiceObject):
	# based on a choiceObject, we will return the results of a given query.  The first element of the choiceObject is the type of query 
	# all/name/death/birth/mpi/range, and the second or third elements are the data to be queried.
	
	if (""+choiceObject[0] == 'all'):												#choiceObject=['all']
		return getAll(conn)				
	if (""+choiceObject[0] == 'name'):												#choiceObject=['name','Jimmie','Ward']
		return getRecordsByName(conn,choiceObject[1],choiceObject[2])
	if (""+choiceObject[0] == 'death'):											#choiceObject=['death','01/23/2012']
		return getRecordsByDeathDate(conn,choiceObject[1])
	if (""+choiceObject[0] == 'birth'):											#choiceObject=['birth','07/24/1918']
		return getRecordsByDOB(conn,choiceObject[1])
	if (""+choiceObject[0] == 'mpi'):												#choiceObject=['mpi','458132379']
		return getRecordsByMPI(conn,choiceObject[1])
	if (""+choiceObject[0] == 'range'):											#choiceObject=['range','03/24/2005','02/14/2007']
		return getRecordsByDeathRange(conn,choiceObject[1],choiceObject[2])
	return []
	
def getCurrentDate():
	# Get current date, day, month, year
	i = datetime.datetime.now()
	year = i.year
	month = i.month
	day = i.day
	date = month+"/"+day+"/"+year
	return date
	
def getmonthbefore(month):
	# Get the month before this one.  The month before January is December.
	m = int(month) - 1
	if (m == 0):
		return "12"
	if (m < 10):
		return "0%d" % m
	return "%d" % m
	
def getmonthafter(month):
	# Get the month after this one.  The month after December is January.
	m = int(month) + 1
	if (m == 13):
		return "01"
	if (m < 10):
		return "0%d" % m
	return "%d" %m
	
def processRecords(records):
	# When processing is implemented, it will process each record to parse to a JSON FHIR object.
	# Until then, output using arrayDump or lineDump
	
	# print(len(records))
	
	# arrayDump(records)
	# lineDump(records)
	htmlWrite(htmlFormat(records))
		
################################################################################################################
# Output functions

def arrayDump(records):
	# Output the data, one field per line, along with the record index parameter and the field name 
	# in the database.
	for i in range(len(records)):
		record = records[i]
		for j in range(len(record)):
			print("[%d] %s: %s" % (j,dohmpi[j],record[j]))
		print("\n")
		
def lineDump(records):
	# Output the data, one row per line, with fields separated by '|' characters.
	outputs=[]
	for i in range(len(records)):
		output=""
		record = records[i]
		for j in range(len(record)):
			if (record[j] != None):
				output = output + "|" + record[j]
		output = output + "|"
		outputs.append(output)

	for i in range(len(outputs)):
		print(outputs[i])
		
def htmlFormat(records):
	# Provide an HTML-formatted list of the record rows, with a checkbox for each row so data can be
	# selected and processed.  
	outputs="<form name='selectSQLrecords' action='http://someurl.foo'><table border='1'><tr><td></td>" # change this action url
	for i in range(len(dohmpi)):
		outputs = outputs+"<td>%s</td>" % dohmpi[i]
	outputs = outputs + "</tr>"
	for i in range(len(records)):
		output="<tr><td><input type='checkbox' value='%d' /></td>" % i
		record = records[i]
		for j in range(len(record)):
			output = output + "<td>" + str(record[j]) + "</td>"
		outputs = outputs + output + "</tr>"
	outputs = outputs + "</table><input type='submit' value='Submit' /></form>"
	
	return outputs
	
def htmlWrite(output,filename='selection.html'):
	file = open(filename,'w')
	file.write(output)
	file.close()
	print("%s written to local drive." % filename)
	
	
##############################################################################################################
# query functions
# all functions have data sanitized except for table name (10/26)

def getAll(conn):
	# get all records 
	# table not sanitized.
	sqlcommand = "SELECT * from %s;" % (table)
	records = postgresGet(conn,sqlcommand)
	return records

def getRecordsByName(conn, fname, lname):
	# given a first name fname and a last name lname, retrieve the first record with that first and last name
	# all data sanitized except table.
	sqlcommand = "SELECT * FROM %s " % (table)
	sqlcommand = sqlcommand + "WHERE provider_first_name = (%s) AND provider_last_name = (%s);" 
	data = [fname,lname]
	records = postgresGet(conn, sqlcommand, data)
	return records
	
def getRecordsByDeathDate(conn, deathdate):
	# given a death date, retrieves all records with a death date equal to the given death date.
	# assume date of form mm/dd/yyyy
	# all data sanitized except table.
	dmonth = deathdate[:2]
	dday = deathdate[3:5]
	dyear = deathdate[-4:]
	sqlcommand = "SELECT * FROM %s " % (table)
	sqlcommand = sqlcommand + "WHERE deathccyy = (%s) AND deathmm = (%s) AND deathdd = (%s);" 
	data = (dyear,dmonth,dday)
	records = postgresGet(conn, sqlcommand, data)
	return records

def getRecordsByDOB(conn,birthdate):
	# given a birth date, retrieve all records with a birthdate equal to the given birth date.
	# assume date of form mm/dd/yyyy
	# all data sanitized except table.
	sqlcommand = "SELECT * from %s " % (table)
	sqlcommand = sqlcommand + "WHERE provider_dob_dob = (%s);" 
	data = (birthdate,)
	records = postgresGet(conn,sqlcommand,data)
	return records
	
def getRecordsByMPI(conn,mpi):
	# given a MPI number, retrieve all records with a MPI value equal to the given mpi.
	# all data sanitized except table.
	mpi = ""+mpi
	sqlcommand = "SELECT * from %s " % (table)
	sqlcommand = sqlcommand + "WHERE statefilenumber = (%s);" 
	data = (mpi,)
	records = postgresGet(conn,sqlcommand,data)
	return records

def getRecordsByDeathRange(conn,start,end):
	# given a start and end death date, returns all records with a death date in that range.
	# assume dates of form mm/dd/yyyy
	# all data sanitized except for table.
	smonth = start[:2]
	sday = start[3:5]
	syear = start[-4:]
	emonth = end[:2]
	eday = end[3:5]
	eyear = end[-4:]

	if (int(eyear) < int(syear)):
		return None
	if (int(eyear) == int(syear) & int(emonth) < int(smonth)):
		return None
	if (int(eyear) == int(syear) & int(emonth) == int(smonth) & int(eday) < int(sday)):
		return None

	command = "SELECT * FROM %s WHERE " % (table)
	data = []
	
	# same year same month date range
	if (int(eyear) == int(syear)): 
		command = command + ("deathccyy = '%s' AND " % (syear))
		# 03/08/2015 - 3/28/2015
		if (int(emonth) == int(smonth)):
			command = command + "deathmm = (%s) AND deathdd BETWEEN (%s) AND (%s);" 
			data.extend([smonth,sday,eday])
		# 03/28/2015 - 04/22/2015
		elif (int(emonth)-int(smonth) == 1):
			command = command + "((deathmm = (%s) AND deathdd BETWEEN (%s) AND '32') OR (deathmm = (%s) AND deathdd BETWEEN '00' AND (%s)));" 
			data.extend([smonth,sday,emonth,eday])
		# 03/28/2015 = 05/22/2015
		else:
			command = command + "((deathmm = (%s) AND deathdd BETWEEN (%s) and '32') OR (deathmm between (%s) AND (%s)) OR (deathmm = (%s) AND deathdd BETWEEN '00' and (%s)));"
			data.extend([smonth,sday,getmonthafter(smonth),getmonthbefore(emonth),emonth,eday])
	else:
		command = command + "(deathccyy = (%s) AND deathmm = (%s) AND deathdd BETWEEN (%s) AND '32') OR " 
		data.extend([syear,smonth,sday])
		if(smonth != "12"):
			command = command + "(deathccyy = (%s) AND deathmm BETWEEN (%s) AND '13') OR " 
			data.extend([syear,getmonthafter(smonth)])
		if(emonth != "01"):
			command = command + "(deathccyy = (%s) AND deathmm BETWEEN '00' AND (%s)) OR "
			data.extend([eyear,getmonthbefore(emonth)])
		command = command + "(deathccyy = (%s) AND deathmm = (%s) AND deathdd BETWEEN '00' AND (%s));"
		data.extend([eyear,emonth,eday])
		if (int(eyear) - int(syear) > 1):
			command = command[:-1] + " OR (deathccyy BETWEEN (%s) AND (%s));" 
			data.extend(["%s"%(int(syear)+1), "%s"%(int(eyear)-1)])
			
	#print (command)													###DEBUG###
	#print (data)
	
	records = postgresGet(conn, command, data)
	return records
####################################################################################################################

def main(arg1=None,arg2=None,arg3=None):
	args = sys.argv
	if (arg1):
		args.append(arg1)
	if (arg2):
		args.append(arg2)
	if (arg3):
		args.append(arg3)
	if (len(args) == 1):
		print("No command line arguments given.")
		printsyntax()
	else:
		dohmpi = postgresGet(makeConn(),"SELECT column_name FROM information_schema.columns WHERE table_name = '%s';" % table)
		choiceObject = args[1:]
		if (checkargs(choiceObject)):
			print ("Connecting to database\n	->%s" % (host))
			conn = makeConn()
			records = queryFromGUI(conn,choiceObject)
			processRecords(records)
		else:
			print "Bad arguments:", choiceObject
			printsyntax()

if __name__ == "__main__":
	main()


