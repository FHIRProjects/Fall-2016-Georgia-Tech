# -*- coding: utf-8 -*-
"""
Tests fhirclient set-up and query

Can't get it to work with local instance.
Has to do with postgres authorization I think.

This is a temporary script file.
"""

from fhirclient import client

settings = {'app_id':'open_app',
            'api_base':'https://fhir-open-api-dstu2.smarthealthit.org'}
            #'api_base':'http://localhost:8080/fhir-omopv5/base/'}

#settings = {'app_id':'fhir-omopv5',
#            'api_base':'http://localhost:8080/fhir-omopv5/base/metadata',
#            'redirect_uri':'http://localhost:8080/fhir-omopv5/',
#            'launch_token':'fhirpass'}
            #'api_base':'http://localhost:8080/fhir-omopv5/base/'}
            
smart = client.FHIRClient(settings=settings)

smart.prepare()

print("Smart Ready: {}".format(smart.ready))

from fhirclient.models import patient as p
from fhirclient.models import observation as o

patient_search = p.Patient.where({'family':'Shaw','given':'Amy'})
patients = patient_search.perform_resources(smart.server)
amy = patients[0]
    
hepatic_loinc_codes = ['2885-2','1751-7','10834-0','1759-0','1975-2','1968-7',
                       '1971-1','6768-6','1920-8','1742-6','20394-3','20392-7',
                       '20393-5']
                       
liver_tests = []
for loinc_code in hepatic_loinc_codes:             
    obs_search = o.Observation.where({'code':loinc_code, 'patient':amy.id})
    liver_tests.extend( obs_search.perform_resources(smart.server) )

print( 'Found {} Liver Tests for {} {}'.format(len(liver_tests),
                                               amy.name[0].given, 
                                               amy.name[0].family) )
for test in liver_tests:
    print( test.as_json() )