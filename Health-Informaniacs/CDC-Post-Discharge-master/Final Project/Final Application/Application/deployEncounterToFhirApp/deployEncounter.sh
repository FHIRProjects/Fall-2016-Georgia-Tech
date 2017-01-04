# Use this script to deploy client side pages for the sample fhir-demo app to display Encounter resources. If you restart
# the service it will pull the original files from the public GitHub repository. So run this script after starting the SMART
* on FHIR service.
cp -fv /media/sf_smart-on-fhir/dev/cdc-pds-reporting/CDC-Post-Discharge/deployEncounterToFhirApp/main.html /home/fhir/apps/static/apps/fhir-demo/app/views/
cp -fv /media/sf_smart-on-fhir/dev/cdc-pds-reporting/CDC-Post-Discharge/deployEncounterToFhirApp/main.js /home/fhir/apps/static/apps/fhir-demo/app/scripts/controllers/
