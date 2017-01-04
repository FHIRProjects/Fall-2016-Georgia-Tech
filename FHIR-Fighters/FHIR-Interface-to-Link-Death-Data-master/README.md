# FHIR-Interface-to-Link-Death-Data
FHIR Interface to Link Death Data to Other Data Sources

## Installation:
To install, use pip to install the following Python libraries first:

> pip install fhirclient

> pip install oauth2client

> pip install psycopg2

> pip install requests

Make sure createPatient.py, DBAccessor.py, doh2fhir.py, and networkConstants.py are in the same folder.
 
Open networkConstants.py in a text editor and make sure the fields for the PostgreSQL server and FHIR server are set.

Note: OAuth2 is not yet supported in the client.

## Syntax:
----
> python doh2fhir.py

Processes all providers for the current date.

----
> python doh2fhir.py mm/dd/yyyy

Processes all providers for the indicated date.

-----
> python doh2fhir.py mm/dd/yyyy mm/dd/yyy

Processes all providers for dates in the indicated range (from start date to end date).

----
> python doh2fhir.py providernumber

Process the provider with the indicated provider number.

----
> python doh2fhir.py fname lname

Processes the provider with the indicated first and last name.

----
> python doh2fhir.py lname

Processes all providers with the indcated last name.