'''
    This script will attempt to open your webbrowser,
    perform OAuth 2 authentication and print your access token.
    It depends on two libraries: oauth2client and gflags.
    To install dependencies from PyPI:
    $ pip install python-gflags oauth2client
    Then run this script:
    $ python get_oauth2_token.py
    
    This is a combination of snippets from:
    https://developers.google.com/api-client-library/python/guide/aaa_oauth
'''

# pip install python-gflags oauth2client

from oauth2client.client import OAuth2WebServerFlow
from oauth2client.tools import run_flow
from oauth2client.file import Storage

import networkConstants as nc

host = "http://%s" % nc.fhirurl
redirect = host+"/auth_return"

flow = OAuth2WebServerFlow(client_id=nc.fhirclient_id,
                           client_secret=nc.fhirclient_secret,
                           scope=host,
                           redirect_uri=redirect)

storage = Storage('creds.data')

credentials = run_flow(flow, storage)

print "access_token: %s" % credentials.access_token