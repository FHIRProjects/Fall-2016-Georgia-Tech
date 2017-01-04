import createPractitioner as c
from DBAccessor import DBAccess 

db = DBAccess()
# get all deaths for the current date.
#record = db.search('death',db.getCurrentDate(),'raw')
record = db.search('all','raw')

for i in range(len(record[1])):
    if i == 0: 
        pass        # this is the dohmpi field list, don't process it as a practitioner.
    elif len(record[1][i]) == 0:
        pass        # the last tuple will be an empty list, don't process it.
    else:
        prac = c.createPractitioner(record[0],record[1][i])
        #
        # Insert code here that adds a practitioner record to the 
        #
        print ("processed practitioner %d" % i)
        print prac.as_json()
print ("task completed")




