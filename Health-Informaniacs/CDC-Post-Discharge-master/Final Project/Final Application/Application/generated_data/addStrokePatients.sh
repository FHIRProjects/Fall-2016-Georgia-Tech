sudo cp -v /media/sf_smart-on-fhir/dev/cdc-pds-reporting/CDC-Post-Discharge/generated_data/*.xml /home/fhir/sample-patients/generated-data/
chmod 777 /home/fhir/sample-patients/generated-data/*.xml
sudo /home/student/installer/provisioning/examples/tasks/reset-database.sh

