From the repository root:

0. Add the following to the /home/student/installer/provisioning/custom_settings.yml file
   * sample_patients_limit: 20
1. Delete the contents of /home/fhir/sample-patients/generated-data
   * rm -rfv /home/fhir/sample-patients/generated-data/*.xml
2. Copy the contents of this directory to /home/fhir/sample-patients/generated-data
   * su root
   * cp -v /media/sf_smart-on-fhir/dev/cdc-pds-reporting/CDC-Post-Discharge/generated_data/*.xml /home/fhir/sample-patients/generated-data/
   * chmod 755 /home/fhir/sample-patients/generated-data/*.xml
   * exit
3. Re-load the local FHIR server with:
   * cd /home/student (or wherever smart-on-fhir sample deployed)
   * sudo ./installer/provisioning/examples/tasks/reset-database.sh

Note: Anytime you restart SMART on FHIR you will need to re-run these steps to reset the fhir database with our custom data. This is because the SMART on FHIR default app recreates the patient data from a different source. The purpose of this step is to override that data.