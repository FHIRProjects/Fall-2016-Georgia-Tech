# FHIR-Interface-to-Directory-of-Clinicians

## Purpose

The purpose of this project is to map the records stored in the Utah Department of Health's (UDOH) Directory of Clinicians database to the Fast Health Interoperability Resource (FHIR) specifications. This will allow developers to access this practitioner licensure data in a standardized format in accordance with the FHIR specifications, currently published in its second version of a Draft Standard for Trial Use (DSTU 2).

## Prerequisites

The following tools must be installed on your server:

* [Maven](https://maven.apache.org/)
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)

This documentation currently covers the use of [Jetty](http://www.eclipse.org/jetty/), an open-source Java server container used in development. The use of other server tools is currently not covered in this documentation

## Getting Started

Download this code as a zip file and extract the resources on your server that will handle these requests.

On the command line, navigate to the project root directory and execute the following commands:

```
# Navigate to project root directory
$ cd ~/FHIR-Interface-to-Directory-of-Clinicians

# Build project
$ mvn clean install

# Navigate to service module
$ cd service

# Start server
$ mvn jetty:run
```

## Upload Data

The application includes a webapp, allowing users to upload practitioner information via a CSV file in bulk. To use this feature, go to http://localhost:8090/bulkUpload.html, select your CSV file, and click "Upload It".

A sample CSV file, bulkUpload_SampleData.csv, has been included in this repository.

## Submitting Requests

Once the server is up and running, you can submit licensure lookup requests to the server using the following format:

#### Licensure Number Lookup

Request Type: GET  
URL Endpoint: http://localhost:8090/fhir/licensure/byLicenseNo?licenseNo={LICENSE_NO}  
URL Parameter: Practitioner license number

#### Practitioner Search

Request Type: GET  
URL Endpoint: http://localhost:8090/fhir/practitioner/json/lookup?LICENSE_NO={LICENSE_NO}  
URL Parameter: Practitioner license number

Request Type: GET  
URL Endpoint: http://localhost:8090/fhir/practitioner/xml/lookup?LICENSE_NO={LICENSE_NO}  
URL Parameter: Practitioner license number

Request Type: GET  
URL Endpoint: http://localhost:8090/fhir/practitioner/json/lookup?LAST_NAME={LAST_NAME}  
URL Parameter: Practitioner's last name

Request Type: GET  
URL Endpoint: http://localhost:8090/fhir/practitioner/xml/lookup?LAST_NAME={LAST_NAME}  
URL Parameter: Practitioner's last name

#### Add Practitioner to Derby DB (Development Only)

Request Type: POST  
URL Endpoint: http://localhost:8090/fhir/licensure/add/  
Data: DoCPDao object

## Deployed Solution

A sample web application has been deployed and ready with sample data at:

https://udoh-doc.herokuapp.com/

Example searches:

https://udoh-doc.herokuapp.com/fhir/practitioner/json/lookup?LICENSE_NO=419857-3239  
https://udoh-doc.herokuapp.com/fhir/practitioner/xml/lookup?LICENSE_NO=419857-3239  
https://udoh-doc.herokuapp.com/fhir/practitioner/json/lookup?LAST_NAME=Clark  
https://udoh-doc.herokuapp.com/fhir/practitioner/xml/lookup?LAST_NAME=Clark  
https://udoh-doc.herokuapp.com/fhir/practitioner/xml/lookup?LAST_NAME=Clark&FIRST_NAME=Robert  
https://udoh-doc.herokuapp.com/fhir/practitioner/xml/lookup?LAST_NAME=Clark&LAST_NAME=Allan  
https://udoh-doc.herokuapp.com/fhir/practitioner/xml/lookup?LAST_NAME=Katz&LAST_NAME=Johnson&FIRST_NAME=Bradley

## About

#### Team DNA  
Aaron Higdon  
Dan Abel  
Dan Frakes  
Nate Smith  
David Vinegar
