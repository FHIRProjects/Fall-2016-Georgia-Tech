from fhirclient import client
settings = {
    'app_id': 'my_web_app',
    'api_base': 'https://fhir-open-api-dstu2.smarthealthit.org'
}
smart = client.FHIRClient(settings=settings)

from DBAccessor import DBAccess
import createPatient as c
import fhirclient.models.patient as p
import json

db = DBAccess()
record = db.getRecordsByName('%','SMITH')
field = db.getDohmpiFieldNames()
pat = c.createPatient(field,record[0])


patient = client.create().resource(pat)
print (patient.as_json())