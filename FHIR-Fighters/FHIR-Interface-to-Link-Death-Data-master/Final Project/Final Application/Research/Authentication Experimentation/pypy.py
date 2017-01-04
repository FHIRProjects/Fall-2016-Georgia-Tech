# https://pypi.python.org/pypi/fhir
#pip install fhir
#pip install lxml

from fhir import *

rest = RestfulFHIR('http://fhirtest.uhn.ca/search?serverId=hapi_dev&pretty=true&' 'json')
params = {'identifier': 125377}
query = rest.search('Patient', params)
print query

print query.text