from pyoauth2.client import OAuth2AuthorizationFlow, FileStorage, OAuth2APIRequest
import networkConstants as nc

class FHIRRequest(OAuth2APIRequest):
    def __init__(self, access_token):
        OAuth2APIRequest.__init__(self, access_token)
        self.authorization_header = {
            "Authorization": "Bearer %s" % self.access_token
            }

        

if __name__ == '__main__':

    fhirrequest = "http://fhir.ehoth.net:9080/fhir/Patient-1"

    required_params = {
        'client_id': nc.fhirclient_id,
        'client_secret': nc.fhirclient_secret,
        'auth_uri': "http://"+nc.fhirurl+":"+nc.fhirauthport+"/oauth2/auth",
        'token_uri': "http://"+nc.fhirurl+":"+nc.fhirauthport+"/oauth2/token",
        'scope': ["http://"+nc.fhirurl+":"+nc.fhiraccessport],
        'redirect_uri': "urn:ietf:wg:oauth:2.0:oob"
        }

    extra_auth_params = {
        'response_type': "code",
        'access_type': "offline"
        }

    extra_token_params = {
        'grant_type': "authorization_code",
        }

    storage = FileStorage('fhiroauth2.dat')
    credentials = storage.get()
    if credentials is None:
        flow = OAuth2AuthorizationFlow(required_params,
                                       extra_auth_params,
                                       extra_token_params,
                                       True)
        flow.retrieve_authorization_code()
        credentials = flow.retrieve_token()
        storage.save(credentials)
    
    access_token = credentials['access_token']
    
    req = FHIRRequest(access_token)
    data = req.request(fhirrequest)
    print data

    