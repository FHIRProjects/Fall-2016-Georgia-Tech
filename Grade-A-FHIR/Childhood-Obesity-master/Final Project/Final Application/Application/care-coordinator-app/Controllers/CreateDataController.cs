using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNet.Mvc;
using System.Data;
using Hl7.Fhir.Rest;
using HL7 = Hl7.Fhir.Model;
using LumenWorks.Framework.IO.Csv;
using System.IO;
using CDC_Obesity_App.Models;

namespace CDC_Obesity_App.Controllers
{
    public class CreateDataController : Controller
    {
        //endpoints
        //MiHIN
        //string endpoint = "http://52.72.172.54:8080/fhir/baseDstu2/";
        //Georgia Tech
        //string endpoint = "http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp/base/";
        //custom
        //string endpoint = "http://104.197.124.155/gt-fhir-webapp/base/";
        //smartfhir
        //string endpoint = "https://fhir-open-api-dstu2.smarthealthit.org";


        Random rand = new Random(123);

        /// <summary>
        /// Create the records and post to the given endpoint
        /// </summary>
        /// <param name="endpointNumber"></param>
        /// <returns></returns>
        public JsonResult CreateAndPostData(int endpointNumber)
        {
            string endpoint = "";

            switch (endpointNumber)
            {
                case 1:
                    //MiHIN
                    endpoint = "http://52.72.172.54:8080/fhir/baseDstu2/";
                    break;
                case 2:
                    //Georgia Tech
                    endpoint = "http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp/base/";
                    break;
                case 3:
                    //custom
                    endpoint = "http://104.197.124.155/gt-fhir-webapp/base/";
                    break;
                case 4:
                    //smartfhir
                    endpoint = "https://fhir-open-api-dstu2.smarthealthit.org";
                    break;
                default:
                    break;
            }


            CreatedDataContainer data = CreateData();

            return PostData(data, endpoint);
        }

        /// <summary>
        /// Try to post the data
        /// </summary>
        /// <param name="data"></param>
        /// <returns>True if successfull</returns>
        public JsonResult PostData(CreatedDataContainer data, string endpoint)
        {
            //establish client
            var fhirClient = new FhirClient(endpoint);

            int patUpdSuccess = 0;
            int obsUpdSuccess = 0;
            int condUpdSuccess = 0;
            int patUpdFail = 0;
            int obsUpdFail = 0;
            int condUpdFail = 0;

            
            //add the patients
            foreach (var patient in data.Patients)
            {
                try
                {
                    fhirClient.Update(patient);
                    patUpdSuccess++;
                }
                catch (Exception ex)
                {
                    patUpdFail++;
                }
            }

            //add the observations
            foreach (var observation in data.Observations)
            {
                try
                {
                    fhirClient.Update(observation);
                    obsUpdSuccess++;
                }
                catch (Exception ex)
                {
                    obsUpdFail++;
                }
            }

            //add the conditions
            foreach (var condition in data.Conditions)
            {
                try
                {
                    fhirClient.Update(condition);
                    condUpdSuccess++;
                }
                catch (Exception ex)
                {
                    condUpdFail++;
                }
            }

            string resultsString = "Patient Updates S:" + patUpdSuccess + " F:" + patUpdFail + Environment.NewLine
                + "Observation Updates S:" + obsUpdSuccess + " F:" + obsUpdFail + Environment.NewLine
                + "Condition Updates S:" + condUpdSuccess + " F:" + condUpdFail + Environment.NewLine;

            return Json(resultsString);
        }

        /// <summary>
        /// Generate data to supplement server data
        /// </summary>
        /// <returns></returns>
        public CreatedDataContainer CreateData()
        {
            //read the state data
            DataTable stateData = ReadStateData();

            DataTable states = stateData.DefaultView.ToTable(true, "State");

            //parameters for the size of the generated data set
            int maxPerState = 10;
            int minPerState = 10;
            int maxObsPerPatient = 5;
            int minObsPerPatien = 1;
            int maxCondPerPatient = 1;

            double conditionProb = 0.15;


            //set the starting patient number
            int patientNumber = 100000;
            int observationNumber = 100000;
            int conditionNumber = 100000;

            //lists to store the generated data
            List<HL7.Patient> patList = new List<HL7.Patient>();
            List<HL7.Observation> obsList = new List<HL7.Observation>();
            List<HL7.Condition> condList = new List<HL7.Condition>();


            //do patient generation per state
            foreach (DataRow state in states.Rows)
            {
                
                //get the countys for the given state
                List<DataRow> counties =
                    (from county in stateData.AsEnumerable()
                    where county.Field<string>("State").Equals(state.Field<string>("State"))
                    select county).ToList<DataRow>();

                //randomly determine the number of patients per state
                int patientCount = Convert.ToInt32(Math.Round(rand.NextDouble() * (maxPerState - minPerState))) + minPerState;

                for(int i=0; i < patientCount; i++)
                {
                    //generate a practitioner id
                    //currently using IDs 1 - 5
                    int practId = rand.Next(1, 5);

                    //select a county
                    DataRow thisCounty = counties[rand.Next(counties.Count())];
                    //generate the new patient
                    HL7.Patient thisPatient = GeneratePatient(patientNumber, practId, thisCounty["State"].ToString(), thisCounty["County"].ToString(), thisCounty["Postal Code"].ToString());
                    //add to the list
                    patList.Add(thisPatient);
                    patientNumber++;
                }
            }

            //generate observations for each patient
            foreach(var patient in patList)
            {
                int obsCount = rand.Next(0, maxObsPerPatient);
                for(int i=0; i < obsCount; i++)
                {
                    HL7.Observation thisObservation = GenerateObservation(observationNumber, patient, i);
                    obsList.Add(thisObservation);
                    observationNumber++;
                }
            }

            //generate conditions for each patient
            foreach(var patient in patList)
            {
                double condPct = rand.NextDouble();
                if(condPct < conditionProb)
                {
                    HL7.Condition thisCondition = GenerateCondition(conditionNumber, patient);
                    condList.Add(thisCondition);
                    conditionNumber++;
                }
            }

            return new CreatedDataContainer(patList, condList, obsList);
        }

        /// <summary>
        /// Reads in list of states and counties from csv
        /// </summary>
        /// <returns></returns>
        public DataTable ReadStateData()
        {
            DataTable dt = new DataTable();
            dt.Columns.Add("Postal Code");
            dt.Columns.Add("State");
            dt.Columns.Add("County");

            using (TextReader reader = System.IO.File.OpenText("us_postal_codes.csv"))
            {
                CsvReader csvReader = new CsvReader(reader, true);

                while (csvReader.ReadNextRecord())
                {
                    DataRow dr = dt.NewRow();
                    dr["Postal Code"] = csvReader["Postal Code"];
                    dr["State"] = csvReader["State"];
                    dr["County"] = csvReader["County"];

                    dt.Rows.Add(dr);
                }
            }

            return dt;
        }

        /// <summary>
        /// creates a list of practitioners to simulate log in 
        /// </summary>
        /// <returns></returns>
        public List<Practitioner> CreatePractitioner()
        {
            List<Practitioner> practitioners = new List<Practitioner>();

            //just create some basic practitioners
            practitioners.Add(new Practitioner(1, "Emory University", true));
            practitioners.Add(new Practitioner(2, "The Johns Hopkins University", true));
            practitioners.Add(new Practitioner(3, "University of Illinois, Chicago", true));
            practitioners.Add(new Practitioner(4, "University of Iowa", false));
            practitioners.Add(new Practitioner(5, "University of Maryland, Baltimore", false));

            return practitioners;
        }

        /// <summary>
        /// Generates a HL7 Patient class
        /// </summary>
        /// <param name="patientNumber"></param>
        /// <param name="practitioner"></param>
        /// <param name="state"></param>
        /// <param name="district"></param>
        /// <param name="postalCode"></param>
        /// <returns></returns>
        public HL7.Patient GeneratePatient(int patientNumber, int practitioner, string state, string  district, string postalCode)
        {
            HL7.Patient genPat = new HL7.Patient();

            //give an id
            genPat.Id = patientNumber.ToString();

            ////give a name 
            HL7.HumanName hName = new HL7.HumanName();
            hName.FamilyElement.Add(new HL7.FhirString("TESTLAST"));
            hName.GivenElement.Add(new HL7.FhirString("TESTFIRST"));
            genPat.Name.Add(hName);

            //add a care provider so we can filter for doctor facing app
            HL7.ResourceReference practRef = new HL7.ResourceReference();
            practRef.ReferenceElement = new HL7.FhirString("Practitioner/" + practitioner);
            genPat.CareProvider.Add(practRef);

            //give an address
            HL7.Address address = new HL7.Address();
            address.Use = HL7.Address.AddressUse.Home;
            address.LineElement.Add(new HL7.FhirString("123 Fake Street"));
            address.City = "FAKETOWN";
            address.District = district;
            address.State = state;
            address.PostalCode = postalCode;
            genPat.Address.Add(address);

            //give a gender
            genPat.Gender = (rand.NextDouble() < .5) ? HL7.AdministrativeGender.Female : HL7.AdministrativeGender.Male;

            //give a DOB
            //randomly generate based on age range 0 - 17
            int years = Convert.ToInt32(Math.Round(16 * rand.NextDouble())) + 1;
            int days = Convert.ToInt32(Math.Round(364 * rand.NextDouble()));
            DateTime dob = new DateTime(2016 - years, 01, 01).AddDays(days);
            HL7.Date hl7DOB = new HL7.Date(dob.ToShortDateString());
            genPat.BirthDateElement = hl7DOB;

            return genPat;
        }

        /// <summary>
        /// Generates a condition (Hypertension)
        /// </summary>
        /// <param name="conditionNumber"></param>
        /// <param name="patient"></param>
        /// <returns></returns>
        public HL7.Condition GenerateCondition(int conditionNumber, HL7.Patient patient)
        {
            HL7.Condition genCond = new HL7.Condition();

            //give an id
            genCond.Id = conditionNumber.ToString();

            //give a patient association
            HL7.ResourceReference patRefence = new HL7.ResourceReference();
            patRefence.ReferenceElement = new HL7.FhirString("Patient/" + Int32.Parse(patient.Id));
            genCond.Patient = patRefence;

            //add a code
            genCond.Code = new HL7.CodeableConcept("http://snomed.info/sct", "59621000", "Essential hypertension");

            //add an onset time
            TimeSpan age = new TimeSpan((long) (DateTime.Now.Subtract(DateTime.Parse(patient.BirthDate)).Ticks * rand.NextDouble()));
            DateTime onsetDate = DateTime.Now.Subtract(age);

            genCond.Onset = new HL7.FhirDateTime(onsetDate.Year, onsetDate.Month, onsetDate.Day);

            return genCond;
        }

        /// <summary>
        /// Generates an observation (BMI, Hemoglobin, Height or Weight)
        /// </summary>
        /// <param name="observationNumber"></param>
        /// <param name="patient"></param>
        /// <param name="encounter"></param>
        /// <returns></returns>
        public HL7.Observation GenerateObservation(int observationNumber, HL7.Patient patient, int encounter)
        {
            HL7.Observation genObs = new HL7.Observation();

            //give an id
            genObs.Id = observationNumber.ToString();

            //status of the observation
            genObs.Status = HL7.Observation.ObservationStatus.Final;

            //add a code
            int obsType = rand.Next(0, 3);
            switch(obsType)
            {
                case 0:
                    genObs.Code = new HL7.CodeableConcept("http://loinc.org", "3141-9", "Body weight Measured");
                    genObs.Value = GenerateWeightValues();
                    break;
                case 1:
                    genObs.Code = new HL7.CodeableConcept("http://loinc.org", "8302-2", "Body height");
                    genObs.Value = GenerateHeightValues();
                    break;
                case 2:
                    genObs.Code = new HL7.CodeableConcept("http://loinc.org", "39156-5", "Body mass index (BMI) [Ratio]");
                    genObs.Value = GenerateBMIValues();
                    break;
                case 3:
                    genObs.Code = new HL7.CodeableConcept("http://loinc.org", "718-7", "Hemoglobin [Mass/volume] in Blood");
                    genObs.Value = GenerateHemoValues();
                    break;
                default:
                    break;
            }

            //add a patient reference
            HL7.ResourceReference patRefence = new HL7.ResourceReference();
            patRefence.ReferenceElement = new HL7.FhirString("Patient/" + Int32.Parse(patient.Id));
            genObs.Subject = patRefence;

            //add an encounter reference
            HL7.ResourceReference encRefence = new HL7.ResourceReference();
            encRefence.ReferenceElement = new HL7.FhirString("Encounter/" + encounter);
            genObs.Encounter = encRefence;

            //add observation date
            genObs.Effective = new HL7.FhirDateTime(2015, 04, 1);

            return genObs;
        }

        /// <summary>
        /// BMI Observation Generation
        /// </summary>
        /// <returns></returns>
        private HL7.Quantity GenerateBMIValues()
        {
            HL7.Quantity valueQuantity = new HL7.Quantity();
            valueQuantity.Value = (decimal)(10.0 * rand.NextDouble() + 24.0);
            valueQuantity.Unit = "kg/m2";
            valueQuantity.System = "http://unitsofmeasure.org";
            valueQuantity.Code = "kg/m2";

            return valueQuantity;
        }

        /// <summary>
        /// Weight Observation Generation
        /// </summary>
        /// <returns></returns>
        private HL7.Quantity GenerateWeightValues()
        {
            HL7.Quantity valueQuantity = new HL7.Quantity();
            valueQuantity.Value = (decimal)(60.0 * rand.NextDouble() + 20.0);
            valueQuantity.Unit = "kg";
            valueQuantity.System = "http://unitsofmeasure.org";
            valueQuantity.Code = "kg";

            return valueQuantity;
        }

        /// <summary>
        /// Height Observation Generation
        /// </summary>
        /// <returns></returns>
        private HL7.Quantity GenerateHeightValues()
        {
            HL7.Quantity valueQuantity = new HL7.Quantity();
            valueQuantity.Value = (decimal)(70.0 * rand.NextDouble() + 110.0);
            valueQuantity.Unit = "cm";
            valueQuantity.System = "http://unitsofmeasure.org";
            valueQuantity.Code = "cm";

            return valueQuantity;
        }

        /// <summary>
        /// Hemoglobin Observation Generation
        /// </summary>
        /// <returns></returns>
        private HL7.Quantity GenerateHemoValues()
        {
            HL7.Quantity valueQuantity = new HL7.Quantity();
            valueQuantity.Value = (decimal) (10 * rand.NextDouble() + 6);
            valueQuantity.Unit = "mg/dL";
            valueQuantity.System = "http://unitsofmeasure.org";
            valueQuantity.Code = "mg/dL";

            return valueQuantity;
        }
    }

    /// <summary>
    /// Object to store generated data
    /// </summary>
    public class CreatedDataContainer
    {
        public List<HL7.Patient> Patients { get; set; }
        public List<HL7.Condition> Conditions { get; set; }
        public List<HL7.Observation> Observations { get; set; }

        public CreatedDataContainer()
        {

        }

        public CreatedDataContainer(List<HL7.Patient> patients, List<HL7.Condition> conditions, List<HL7.Observation> observations)
        {
            Patients = patients;
            Observations = observations;
            Conditions = conditions;
        }
    }
}
