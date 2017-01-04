from DBAccessor import DBAccess

db = DBAccess()
print db.getDohmpiFieldNames()
print(db.search('all','line'))
#print(db.search('birth','1/4/1949','array'))
#print(db.search('death','6/7/2016','raw'))
#print(db.search('range','10/14/2016','12/17/2016','line'))
#print(db.search('mpi','201626406','array'))
#print(db.search('name','TAWNLETHIA','WIMMER','line'))
#print(db.search('lname','JONES','html'))
#print(db.search('death',db.getCurrentDate(),'line'))
#db.htmlWrite(db.search('range','1/1/2016','12/31/2016','html'),'FY2016.htm')
