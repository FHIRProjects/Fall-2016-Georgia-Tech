import createPatient as c
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
        pat = c.createPatient(record[0],record[1][i])
        #
        # Insert code here that adds a patient record to the 
        #
        print ("processed patient %d" % i)
        print pat.as_json()
print ("task completed")



