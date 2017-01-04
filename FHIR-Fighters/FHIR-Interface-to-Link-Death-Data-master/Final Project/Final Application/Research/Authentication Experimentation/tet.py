import networkConstants
import base64

authport = networkConstants.fhirauthport
accessport = networkConstants.fhiraccessport
host = networkConstants.fhirurl
user = networkConstants.fhiruser
password = networkConstants.fhirpassword
authpath= "/authorize"


import requests
#POST https://api.oauth2server.com/token
#    grant_type=password&
#    username=USERNAME&
#    password=PASSWORD&
#    client_id=CLIENT_ID
    
#    http://fhir.ehoth.net:9085/openid-connect-server-webapp/authorize

authurl = "http://"+host+":"+authport+"/"#+authpath
#print authurl


basic = base64.b64encode("%s:%s" % (networkConstants.client_id,networkConstants.fhirclient_secret))
header = { 'Authorization': 'Basic %s' % basic }
r = requests.get(authurl,headers=header)
print r.text
#params = {'grant_type': 'password', 'user': user, 'password': password, 'client_id': 'fhirfighters'}
#r = requests.post(authurl, data=params)
#print r.text

#url = "http://fhir.ehoth.net:9085/authorize?response_type=code&client_id=Fhirfighters&redirect_uri=http%%3A%%2F%%2Ffhir.ehoth.net%%3A9080%%2Fafter-auth&scope=patient%%2FObservation.read+patient%%2Fread+openid+profile&state=98wrghuwuogerg97&aud=http%%3A%%2F%%2Ffhir.ehoth.net%%3A9080%%2Ffhir&j_username=%s&j_password=%s" % (user,password)
#url = "http://fhir.ehoth.net:9085/j_spring_security_check?j_username=%s&j_password=%s" % (user,password)
#r = requests.post(url)
#print r.text