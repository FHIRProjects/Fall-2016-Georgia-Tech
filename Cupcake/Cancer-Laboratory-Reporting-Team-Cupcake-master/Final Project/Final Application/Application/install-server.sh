#!/bin/bash

clear
echo "=============================="
echo "= Installing Hapi-Cli Server ="
echo "=============================="
	CURR=$(pwd)
	mkdir ${CURR}/CLIPkg &&
	cd ${CURR}/CLIPkg &&
	wget https://github.com/jamesagnew/hapi-fhir/releases/download/v2.1/hapi-fhir-2.1-cli.tar.bz2 &&
	cd ${CURR} &&
	cp -R ${CURR}/CLIPkg/hapi-fhir-2.1-cli.tar.bz2 /opt &&
	rm -rf ${CURR}/CLIPkg/ &&
	cd /opt &&
	bzip2 -dc /opt/hapi-fhir-2.1-cli.tar.bz2 | tar -xvf - &&
	rm /opt/hapi-fhir-2.1-cli.tar.bz2 &&
	cd ${CURR} &&
	cat > ./run-server.sh << EOT
	#!/bin/bash
	
	echo "===================="
	echo "= Running Hapi-CLI ="
	echo "===================="
	nohup java -jar /opt/hapi-fhir-cli.jar run-server --allow-external-refs & 
EOT
	cat > ./install-profiles.sh << EOT
	#!/bin/bash

	echo "======================="
	echo "= Installing profiles ="
	echo "======================="

	curl http://localhost:8080/baseDstu2/StructureDefinition/CancerPatient --upload-file ./Profiles/CancerPatient.structuredefinition.xml &&
	curl http://localhost:8080/baseDstu2/StructureDefinition/CancerPractitioner --upload-file ./Profiles/CancerPractitioner.structuredefinition.xml &&
	curl http://localhost:8080/baseDstu2/StructureDefinition/CancerDiagnosis --upload-file ./Profiles/CancerDiagnosis.structuredefinition.xml

EOT
	echo "DONE!"
