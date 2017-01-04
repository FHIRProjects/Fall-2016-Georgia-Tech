from fhirclient import client
import networkConstants as nc
import requests
settings = {
 'app_id': 'Fhirfighters',
 'api_base': 'http://polaris.i3l.gatech.edu:8080/fhir-omopv5/base/Patient'
}


#smart = client.FHIRClient(settings=settings)
#print(smart.prepare())

#import fhirclient.models.patient as p
#patient = p.Patient.read('hca-pat-1', smart.server)
#print patient
#patient.birthDate.isostring
# '1963-06-12'
#smart.human_name(patient.name[0])
#'Christy Ebert'

#payload={
#    'grant_type': 'authorization_code', 
#    'client_id': nc.client_id,
#    'client-secret' : nc.client_secret,
#    
#}#

from DBAccessor import DBAccess
import createPatient as c
db = DBAccess()
record = db.search('death','3/29/2016','raw')
print record
payload = ""
for i in range(len(record)):
    if i == 0: 
        print ("A")
        pass        # this is the dohmpi field list, don't process it as a practitioner.
    elif len(record[i]) == 0:
        print("B")
        pass        # the last tuple will be an empty list, don't process it.
    else:
        pat = c.createPatient(record[0],record[i])
        #
        # Insert code here that adds a patient record to the 
        #
        payload = pat.as_json()
print payload
header = {"Content-Type":"application/json+fhir"}
url = 'http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp/base/Patient'

response = requests.post(url, data=payload, headers=header)

print response.text