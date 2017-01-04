#!/usr/bin/python

# To use:
# pip install psycopg2

# FHIRFighters - GTCS6440
#	Peter Graening
#	Tony Potter
#	Shashindra Pradhan
#	Ali Shaikh
#	Radha Venkataraman

import psycopg2
import sys
import datetime
import networkConstants

class DBAccess:

	def __init__(self):
		self.initialize()
	
	def initialize(self):
		# Fetch DB Constants from networkConstants.py.  
		# DO NOT HARD-CODE CONSTANTS HERE.
		
		self.host = networkConstants.dbhost                      
		self.dbname = networkConstants.dbname
		self.user = networkConstants.dbuser
		self.password = networkConstants.dbpassword
		self.table = networkConstants.dbtable
		
		# Create a connection, which we'll base future cursors off of.
		conn_string = "host=%s dbname=%s user=%s password=%s" % (self.host,self.dbname,self.user,self.password)
		self.conn = psycopg2.connect(conn_string)
		

	def postgresGet(self,sqlcommand,data=None):
	################################################################################
	# Send a postgres command to the db server, with data set separately
	# This allows us to sanitize our inputs.
	#
	# @param sqlcommand -	the sql command to be executed (with data fields to be 
	#						sanitized as (%s).
	# @param data		-	the data parameters to be sanitized (optional).
	#
	# @return				a cursor object with all records matching the sql 
	#						command.
	################################################################################ 
		cursor = self.conn.cursor()
		#print sqlcommand, data
		if (data):
			cursor.execute(sqlcommand,data)
		else:
			cursor.execute(sqlcommand)
		return cursor.fetchall()
		
	def getDohmpiFieldNames(self):
	################################################################################
	# Get the current list of column headers from the DOHMPI server.
	#
	#
	# @return				a list containing all dohmpi column headers .
	################################################################################ 
		sqlcommand = "SELECT column_name FROM information_schema.columns WHERE table_name = '%s';" % self.table 
		return self.postgresGet(sqlcommand)

	def isdate(self,date):
	################################################################################
	# Determines if a data parameter is formatted correctly.
	# 
	# @return				True or False, depending on proper format.
	################################################################################
		if (len(date) > 10):
			return False
		if (not "/" in date):
			return False
		first = date.index("/")
		second = date.index("/",first+1)
		if (first == -1 or second == -1):
			return False
		mo = int(date[0:first])
		dy = int(date[first+1:second])
		yr = int(date[second+1:])
		
		if (mo < 0 or mo > 12):
			raise TypeError("Improperly formatted month, isdate(date)")
		if (yr < 1900 or yr > 2100):
			raise TypeError("Improperly formatted year, isdate(date)")
		if (dy < 0 or dy > 31):
			raise TypeError("Improperly formatted day, isdate(date)")
		if (dy == 31 and mo in [2,4,6,9,11]):
			raise TypeError("Improperly formatted short month, isdate(date)")
		if (mo == 2 and dy == 30):
			raise TypeError("Improperly formatted February, date(date)")
		if (mo == 2 and dy == 29 and yr % 4 != 0):
			raise TypeError("Improperly formatted February leapday, isdate(date)")
		return True
			
	def checkargs(self, args):
	################################################################################
	# Determines if appropriate arguments have been passed based on what we allow
	# users to search for: 'all', 'birth'date, 'death'date, death'range', 'mpi'
	# or first and last 'name'.
	#
	# @param args			The command line arguments passed to our object.
	# 
	# @return				True or False, depending on proper format.
	################################################################################
		if (args[0] == 'all' and len(args) == 1):
			return True
		if ((args[0] == 'birth' or args[0] == 'death') and len(args) == 2 and self.isdate(args[1])):
			return True
		if (args[0] == 'range' and len(args) == 3 and self.isdate(args[1]) and self.isdate(args[2])):
			return True
		if (args[0] == 'mpi' and len(args) == 2 and int(args[1])):
			return True
		if (args[0] == 'name' and len(args) == 3):
			return True
		if (args[-1] in ['array','line','html','raw']):
			return True
		return False

	def printsyntax(self):
	################################################################################
	# Returns the proper formatting for users to follow when using our command line.
	# 
	# @return				A string that represents acceptable formatting.
	################################################################################
		out = "Syntax:\n"
		out = "  dbAccess.search('all','html')\n"
		out = "  dbAccess.search('birth','01/01/1950','array')\n"
		out = "  dbAccess.search('death','02/02/2010','line')\n"
		out = "  dbAccess.search('range','03/03/2005','04/04/2005','html')\n"
		out = "  dbAccess.search('mpi','123456789','array')\n"
		out = "  dbAccess.search('name','Fred','Flintstone','line')\n"
		raise SyntaxError(out)
	
	def queryFromGUI(self,choiceObject):
	################################################################################
	# Parses the choice object to apply the appropriate query to the sql server.
	#
	# @param choiceObject - A list which contains the search type and search terms.
	# 
	# @return				A list of lists of records that match the search.
	################################################################################
		if (""+choiceObject[0] == 'all'):											#choiceObject=['all']
			return self.getAll()				
		if (""+choiceObject[0] == 'name'):											#choiceObject=['name','Jimmie','Ward']
			return self.getRecordsByName(choiceObject[1],choiceObject[2])
		if (""+choiceObject[0] == 'death'):											#choiceObject=['death','01/23/2012']
			return self.getRecordsByDeathDate(choiceObject[1])
		if (""+choiceObject[0] == 'birth'):											#choiceObject=['birth','07/24/1918']
			return self.getRecordsByDOB(choiceObject[1])
		if (""+choiceObject[0] == 'mpi'):											#choiceObject=['mpi','458132379']
			return self.getRecordsByMPI(choiceObject[1])
		if (""+choiceObject[0] == 'range'):											#choiceObject=['range','03/24/2005','02/14/2007']
			return self.getRecordsByDeathRange(choiceObject[1],choiceObject[2])
		if (""+choiceObject[0] == 'lname'):											#choiceObject=['lname','Ward']
			return self.getRecordsByName("%",choiceObject[1])
		raise SyntaxError("Bad search argument: %s." % choiceObject[0])
		
	def getCurrentDate(self):
	################################################################################
	# Returns the current date in proper format.
	# 
	# @return				A string representing today's date.
	################################################################################
		# Get current date, day, month, year
		i = datetime.datetime.now()
		year = str(i.year)
		month = str(i.month)
		day = str(i.day)
		date = month+"/"+day+"/"+year
		return date
		
	def getmonthbefore(self, month):
	################################################################################
	# Returns the month before this one. The month before January is December.
	# 
	# @return				A string representing the previous month.
	################################################################################
		# Get the month before this one.  
		m = int(month) - 1
		if (m == 0):
			return "12"
		return "%d" % m
		
	def getmonthafter(self, month):
	################################################################################
	# Returns the month after this one. The month after December is January.
	# 
	# @return				A string representing the next month.
	################################################################################
		m = int(month) + 1
		if (m == 13):
			return "1"

		return "%d" %m
	
	def arrayDump(self, records):
	################################################################################
	# Returns each index, fieldname, and field in long form, one field on a line
	#
	# @param records		The records to be printed.
	# 
	# @return				A string representing the records in long form.
	################################################################################
		out = ""
		dohmpi = self.getDohmpiFieldNames()
		for i in range(len(records)):
			record = records[i]
			for j in range(len(record)):
				out = out + ("[%d] %s: %s" % (j,dohmpi[j],str(record[j])))
				out = out + ("\n")
			out = out + ("\n")
		return out
			
	def lineDump(self, records):
	################################################################################
	# Returns each field in the record in short form, one record on a line
	#
	# @param records		The records to be printed.
	# 
	# @return				A string representing the records in short form.
	################################################################################
		outputs=[]
		for i in range(len(records)):
			output=""
			record = records[i]
			for j in range(len(record)):
				if (record[j] != None):
					output = output + "|" +str(record[j])
			output = output + "|"
			outputs.append(output)
	
		out = ""
		for i in range(len(outputs)):
			out = out + str(outputs[i]) + "\n"
		return out
			
	def htmlFormat(self, records):
	################################################################################
	# Returns an HTML formatted table of records matching the indicated query.
	# Checkboxes are added so selections can be made.
	#
	# @param records		The records to be printed.
	# 
	# @return				A string representing the records in HTML form.
	################################################################################
		outputs="<form name='selectSQLrecords' action='http://someurl.foo'><table border='1'><tr><td></td>" # change this action url
		dohmpi = self.getDohmpiFieldNames()
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
		
	def htmlWrite(self, output,filename='selection.html'):
	################################################################################
	# Creates an HTML file for the given output
	#
	# @param output			The HTML formatted records to be saved.
	# @param filename		The filename to be saved (option, default is selection.html)
	# 
	################################################################################
		file = open(filename,'w')
		file.write(output)
		file.close()
		
	def getAll(self):
	################################################################################
	# A query retriving all rows of the database.
	#
	# @return 				A list of lists of matching records.
	################################################################################
		sqlcommand = "SELECT * from %s;" % (self.table)
		records = self.postgresGet(sqlcommand)
		return records
	
	def getRecordsByName(self, fname, lname):
	################################################################################
	# A query retriving all rows of the database with given first and last name.
	#
	# @param fname			The first name to search for.
	# @param lname			The last name to search for.
	#
	# @return 				A list of lists of matching records.
	################################################################################
		sqlcommand = "SELECT * FROM %s " % (self.table)
		sqlcommand = sqlcommand + "WHERE provider_first LIKE (%s) AND provider_last = (%s);" 
		data = [fname,lname]
		records = self.postgresGet(sqlcommand, data)
		return records
		
	def getRecordsByDeathDate(self, deathdate):
	################################################################################
	# A query retriving all rows of the database with given date of death.
	#
	# @param deathdate			The death date to search for.
	#
	# @return 					A list of lists of matching records
	################################################################################
		if (self.isdate(deathdate)):
			first = deathdate.index("/")
			second = deathdate.index("/",first+1)
			dmonth = deathdate[:first]
			dday = deathdate[first+1:second]
			dyear = deathdate[-4:]
			sqlcommand = "SELECT * FROM %s " % (self.table)
			sqlcommand = sqlcommand + "WHERE deathccyy = (%s) AND deathmm = (%s) AND deathdd = (%s);" 
			data = (dyear,dmonth,dday)
			records = self.postgresGet(sqlcommand, data)
			return records
		else:
			raise SyntaxError ("Improperly formatted death date: %s" % deathdate)

	
	def getRecordsByDOB(self,birthdate):
	################################################################################
	# A query retriving all rows of the database with given date of birth
	#
	# @param birthdate			The birth date to search for.
	#
	# @return 					A list of lists of matching records
	################################################################################
		sqlcommand = "SELECT * from %s " % (self.table)
		sqlcommand = sqlcommand + "WHERE provider_dob_dob = (%s);" 
		data = (birthdate,)
		records = self.postgresGet(sqlcommand,data)
		return records
		
	def getRecordsByMPI(self,mpi):
	################################################################################
	# A query retriving all rows of the database with given MPI number
	#
	# @param mpi			The Master Person Index number to search for.
	#
	# @return 				A list of lists of matching records
	################################################################################
		mpi = ""+mpi
		sqlcommand = "SELECT * from %s " % (self.table)
		sqlcommand = sqlcommand + "WHERE provider_license_no = (%s);" 
		data = (mpi,)
		records = self.postgresGet(sqlcommand,data)
		return records
	
	def getRecordsByDeathRange(self,start,end):
	################################################################################
	# A query retriving all rows of the database with death date in given ragne
	#
	# @param start			The first name to search for.
	# @param end			The last name to search for.
	#
	# @return 				A list of lists of matching records
	################################################################################
		if (not self.isdate(start)):
			raise SyntaxError("Improper Start Date in Range: %s" % start)
		if (not self.isdate(end)):
			raise SyntaxError("Improper End Date in Range: %s" % end)
		sfirst = start.index("/")
		ssecond = start.index("/",sfirst+1)
		efirst = end.index("/")
		esecond = end.index("/",efirst+1)
		smonth = start[:sfirst]
		sday = start[sfirst+1:ssecond]
		syear = start[-4:]
		emonth = end[:efirst]
		eday = end[efirst+1:esecond]
		eyear = end[-4:]
	
		if (int(eyear) < int(syear)):
			return None
		if (int(eyear) == int(syear) & int(emonth) < int(smonth)):
			return None
		if (int(eyear) == int(syear) & int(emonth) == int(smonth) & int(eday) < int(sday)):
			return None
	
		command = "SELECT * FROM %s WHERE " % (self.table)
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
				command = command + "((deathmm = (%s) AND deathdd BETWEEN (%s) AND '32') OR (deathmm = (%s) AND deathdd BETWEEN '0' AND (%s)));" 
				data.extend([smonth,sday,emonth,eday])
			# 03/28/2015 = 05/22/2015
			else:
				command = command + "((deathmm = (%s) AND deathdd BETWEEN (%s) and '32') OR (deathmm between (%s) AND (%s)) OR (deathmm = (%s) AND deathdd BETWEEN '00' and (%s)));"
				data.extend([smonth,sday,self.getmonthafter(smonth),self.getmonthbefore(emonth),emonth,eday])
		else:
			command = command + "(deathccyy = (%s) AND deathmm = (%s) AND deathdd BETWEEN (%s) AND '32') OR " 
			data.extend([syear,smonth,sday])
			if(smonth != "12"):
				command = command + "(deathccyy = (%s) AND deathmm BETWEEN (%s) AND '13') OR " 
				data.extend([syear,self.getmonthafter(smonth)])
			if(emonth != "01"):
				command = command + "(deathccyy = (%s) AND deathmm BETWEEN '00' AND (%s)) OR "
				data.extend([eyear,self.getmonthbefore(emonth)])
			command = command + "(deathccyy = (%s) AND deathmm = (%s) AND deathdd BETWEEN '00' AND (%s));"
			data.extend([eyear,emonth,eday])
			if (int(eyear) - int(syear) > 1):
				command = command[:-1] + " OR (deathccyy BETWEEN (%s) AND (%s));" 
				data.extend(["%s"%(int(syear)+1), "%s"%(int(eyear)-1)])
		
		records = self.postgresGet(command, data)
		return records

	def search(self,arg1=None,arg2=None,arg3=None,arg4=None):
	################################################################################
	# A generic search command to search db, specifying search type, parameters, and
	# preferred output format.
	#
	# The first parameter is the search type, either 'all', 'birth'date, 'death'date,
	# 'mpi' value, first and last 'name', 'lname' last name alone, or a death date 
	# 'range'.
	# 
	# The next parameter(s) is/are the search values, such as birth or death date or
	# mpi value (one parameter) or first and last name, or death date range start and
	# end (two parameters).  getCurrentDate() will retrieve the current date 
	# appropriately formatted.
	#
	# The last parameter is the prefered output format, either an itemized 'array'
	# listing, data rows in 'line' format, an 'html'-formatted version, or a 'raw'
	# list with DOHMPI field names as row 0.
	#
	#
	# In the case of an HTML output, htmlWrite() will write an HTML-formatted
	# output to a specified output file, or default to "selection.html".
	#
	# Sample calls will look like:
	#	search('all','line')
	#	search('birth','06/13/1972','array')
	#	search('death','03/03/2003','html')
	#	search('mpi','1234567890','array')
	#	search('name','Fred','Flintstone','line')
	#	search('range','02/14/2006','08/16/2012','html')
	#   search('lname','Garcia','line')
	#	search('range','12/12/2012', '01/01/2015','raw')
	#	search('death',getCurrentDate(),'line')
	#	htmlWrite(search('range','10/01/2015','09/30/2016','html'),'FY2015.htm')
	#
	# @return 				A string representation of matching records, in the
	#						desired format
	################################################################################
		self.initialize()
		args = []
		if (arg1):
			args.append(arg1)
		if (arg2):
			args.append(arg2)
		if (arg3):
			args.append(arg3)
		if (arg4):
			args.append(arg4)
		if (len(args) == 0):
			raise SyntaxError("No arguments present.")
		else:
			choiceObject = args
			if (self.checkargs(choiceObject)):
				records = self.queryFromGUI(choiceObject)
				if (args[-1] == 'array'):
					return self.arrayDump(records)
				elif (args[-1] == 'line'):
					return self.lineDump(records)
				elif (args[-1] == 'html'):
					return self.htmlFormat(records)
				elif (args[-1] == 'raw'):
					dohmpi = [self.getDohmpiFieldNames(),records]
					return dohmpi
				else:
					raise SyntaxError("Improper output specified: %s" % str(args[-1]))
			else:
				raise SyntaxError("Bad arguments: %s" % str(choiceObject))
