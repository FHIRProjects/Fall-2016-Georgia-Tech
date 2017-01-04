#!/bin/bash
while getopts ":s:" opt; do
  case $opt in
    s)
      SERVER=$OPTARG
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done
if [ -z ${SERVER} ]
   then
      echo "No Server Specified"
      exit
fi
for filename in valueSets/*.json; do
    BASEPATH= $(basename "$filename")
    FILE="${BASEPATH%.*}"
    curl ${SERVER}/ValueSet/${FILE} --upload-file ${filename}
done
for filename in extensions/*.json; do
    BASEPATH= $(basename "$filename")
    FILE="${BASEPATH%.*}"
    curl ${SERVER}/StructureDefinition/${FILE} --upload-file ${filename}
done
for filename in profiles/*.json; do
    BASEPATH=$(basename "$filename")
    FILE="${BASEPATH%.*}"
    curl ${SERVER}/StructureDefinition/${FILE} --upload-file ${filename}
done
exit
