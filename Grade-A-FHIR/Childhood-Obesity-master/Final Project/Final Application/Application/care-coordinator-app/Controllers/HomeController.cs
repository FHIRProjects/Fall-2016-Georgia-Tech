using System;
using Microsoft.AspNet.Mvc;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Collections.Generic;
using Hl7.Fhir.Rest;
using System.IO;
using System.Linq;
using System.Data;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Microsoft.AspNet.Http;

//to avoid overlaping namespaces
using HL7 = Hl7.Fhir.Model;
using CDC_Obesity_App.Models;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace CDC_Obesity_App.Controllers
{
    public class HomeController : Controller
    {
        //endpoints
        //MiHIN
        //string endpoint = "http://52.72.172.54:8080/fhir/baseDstu2/";
        //Georgia Tech
        string endpoint = "http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp/base/";
        //custom
        //string endpoint = "http://104.197.124.155/gt-fhir-webapp/base/";
        //smartfhir
        //string endpoint = "https://fhir-open-api-dstu2.smarthealthit.org";

        /// <summary>
        /// Observation codes for queries
        /// </summary>
        string bmiCode = "39156-5";
        string hemoglobinCode = "718-7";
        string heightCode = "8302-2";
        string weightCode = "3141-9";

        

        List<Patient> patients = new List<Patient>();

        private void getPctElevatedHgb(List<HL7.Observation> hgbObservations)
        {
            double elevatedHGBLevel = 0.057;

            //var results = hgbObservations.Where(e => e.Value > elevatedHGBLevel && e.Code. == hemoglobinCode);
        }

        private void getBMIPct(List<HL7.Observation> bmiObservations)
        {
            var results = bmiObservations.Where(e => e.Value.ToString() == "High" && e.Code.ToString() == bmiCode);


        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="startDOB">format-"YYYY-MM-DD"</param>
        /// <param name="endDOB">format-"YYYY-MM-DD"</param>
        /// <returns></returns>
        private List<HL7.Patient> requestAllPatients(string startDOB = null, string endDOB = null)
        {
            //create the return list
            List<HL7.Patient> patientList = new List<HL7.Patient>();
            patients.Clear();
            //establish client
            var fhirClient = new FhirClient(endpoint);

            //set up search parameters
            SearchParams q = new SearchParams();
            q.Add("_count", "1000");

            DateTime outDT;
            if(startDOB != null && DateTime.TryParse(startDOB, out outDT))
            {
                q.Add("birthdate", "<=" + startDOB);
            }

            if(endDOB != null && DateTime.TryParse(endDOB, out outDT))
            {
                q.Add("birthdate", ">=" + endDOB);
            }

            //get the patient bundle        
            HL7.Bundle patientsBundle = null;

            do
            {
                //query the data
                patientsBundle = (patientsBundle == null) ? fhirClient.Search(q, "Patient") : fhirClient.Continue(patientsBundle);

                if (patientsBundle != null)
                {
                    //convert the bundle elements
                    foreach (var entry in patientsBundle.Entry)
                    {
                        patientList.Add((HL7.Patient)entry.Resource);
                        //patients.Add(new Patient((HL7.Patient)entry.Resource));
                    }
                }

            } while (patientsBundle.NextLink != null);

            return patientList;
        }

        private List<HL7.Observation> requestAllObservations(string startDOB = null, string endDOB = null, string obsCode = null, int maxLoops = 4)
        {
            //create the return list
            List<HL7.Observation> observationList = new List<HL7.Observation>();

            //establish client
            var fhirClient = new FhirClient(endpoint);

            //set up search parameters
            SearchParams q = new SearchParams();
            q.Add("_count", "1000");

            DateTime outDT;
            if (startDOB != null && DateTime.TryParse(startDOB, out outDT))
            {
                q.Add("patient.birthdate", "<=" + startDOB);
            }

            if (endDOB != null && DateTime.TryParse(endDOB, out outDT))
            {
                q.Add("patient.birthdate", ">=" + endDOB);
            }

            if(obsCode != null)
            {
                q.Add("code", obsCode);
            }

            //get the observation bundle  
            HL7.Bundle observationBundle = null;

            //set the max loops if provided
            int loops = 0;
            

            do
            {
                //query the data
                observationBundle = (observationBundle == null) ? fhirClient.Search(q, "Observation") : fhirClient.Continue(observationBundle);

                if (observationBundle != null)
                {
                    //convert the bundle elements
                    foreach (var entry in observationBundle.Entry)
                    {
                        observationList.Add((HL7.Observation)entry.Resource);
                    }
                }

                loops++;

            } while (observationBundle.NextLink != null && loops < maxLoops);

            return observationList;
        }

        public List<HL7.Observation> requestPatientObservations(string PatientID)
        {
            //http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp/base/Observation?patient=1

            //create the return list
            List<HL7.Observation> observationList = new List<HL7.Observation>();

            //establish client
            var fhirClient = new FhirClient(endpoint);

            //set up search parameters
            SearchParams q = new SearchParams();
            q.Add("_count", "1000");
            q.Add("patient", PatientID);
            
            //get the observation bundle  
            HL7.Bundle observationBundle = null;

            do
            {
                //query the data
                observationBundle = (observationBundle == null) ? fhirClient.Search(q, "Observation") : fhirClient.Continue(observationBundle);

                if (observationBundle != null)
                {
                    //convert the bundle elements
                    foreach (var entry in observationBundle.Entry)
                    {
                        observationList.Add((HL7.Observation)entry.Resource);
                    }
                }

            } while (observationBundle.NextLink != null);

            return observationList;
        }

        private List<HL7.Condition> requestAllConditions()
        {
            //create the return list
            List<HL7.Condition> conditionList = new List<HL7.Condition>();

            //establish client
            var fhirClient = new FhirClient(endpoint);

            //set up search parameters
            SearchParams q = new SearchParams();
            q.Add("_count", "1000");

            //get the condition bundle  
            HL7.Bundle conditionBundle = null;

            do
            {
                //query the data
                conditionBundle = (conditionBundle == null) ? fhirClient.Search(q, "Condition") : fhirClient.Continue(conditionBundle);

                if (conditionBundle != null)
                {
                    //convert the bundle elements
                    foreach (var entry in conditionBundle.Entry)
                    {
                        conditionList.Add((HL7.Condition)entry.Resource);
                    }
                }

            } while (conditionBundle.NextLink != null);

            return conditionList;
        }

        public List<HL7.Condition> requestPatientConditions(string PatientID)
        {
            //create the return list
            List<HL7.Condition> conditionList = new List<HL7.Condition>();

            //establish client
            var fhirClient = new FhirClient(endpoint);

            //set up search parameters
            SearchParams q = new SearchParams();
            q.Add("_count", "1000");
            q.Add("patient", PatientID);

            //get the observation bundle  
            HL7.Bundle conditionBundle = null;

            do
            {
                //query the data
                conditionBundle = (conditionBundle == null) ? fhirClient.Search(q, "Condition") : fhirClient.Continue(conditionBundle);

                if (conditionBundle != null)
                {
                    //convert the bundle elements
                    foreach (var entry in conditionBundle.Entry)
                    {
                        conditionList.Add((HL7.Condition)entry.Resource);
                    }
                }

            } while (conditionBundle.NextLink != null);

            return conditionList;
        }

        private Patient convertPatientObj(HL7.Patient patient)
        {
            Patient convertedPatient = new Patient(patient);
            
            //TODO: Convert data
            
            return null;
        }

        //[HttpGet]
        //public JsonResult GetTestData()
        //{
        //    Grid testGrid = new Grid();
        //    testGrid.AddRow("1", "Joe", "Smith",  10, "Caucasian", "172.72", "160", "24", "7.9", "false");
        //    testGrid.AddRow("2", "Jenny", "Thompson", 8, "Caucasian", "172.72", "160", "24", "7.9", "false");

        //    Random rand = new Random();

        //    LineChart testChart1 = new LineChart("series-1", "BMI");
        //    for (int i = 0; i < 20; i++)
        //    {
        //        double testBMI = 15.5 + (40 - 15.5) * rand.NextDouble();
        //        DateTime date = DateTime.Now.AddDays((rand.Next(-100, 100)));
        //        testChart1.AddDataPoint(date, testBMI);
        //    }

        //    LineChart testChart2 = new LineChart("series-2", "Hemoglobin");
        //    for (int i = 0; i < 20; i++)
        //    {
        //        double testBMI = 15.5 + (40 - 15.5) * rand.NextDouble();
        //        DateTime date = DateTime.Now.AddDays((rand.Next(-100, 100)));
        //        testChart2.AddDataPoint(date, testBMI);
        //    }

        //    CreateDataController cd = new CreateDataController();
        //    DataTable stateData = cd.ReadStateData();

        //    MapChart testMap1 = new MapChart("BMI");
        //    MapChart testMap2 = new MapChart("Hemoglobin");
        //    //do patient generation per state
        //    DataTable states = stateData.DefaultView.ToTable(true, "State");
        //    foreach (DataRow state in states.Rows)
        //    {
        //        if (state.Field<string>("State").Equals(""))
        //        {
        //            continue;
        //        }

        //        //get the counties for the given state
        //        List<DataRow> counties =
        //            (from county in stateData.AsEnumerable()
        //             where county.Field<string>("State").Equals(state.Field<string>("State"))
        //             select county).ToList<DataRow>();


        //        //update the state value
        //        testMap1.UpdateState(state.Field<string>("State"), rand.NextDouble() / 3);
        //        testMap2.UpdateState(state.Field<string>("State"), rand.NextDouble() / 3);

        //        //update the counties
        //        for(int i=0; i < 5; i++)
        //        {
        //            var county = counties[i];

        //            if (county.Field<string>("County").Equals(""))
        //            {
        //                continue;
        //            }

        //            testMap1.States.Find(x => x.Name.Equals(county.Field<string>("State"))).AddCounty(new USCounty(county.Field<string>("County"), rand.NextDouble() / 3));
        //            testMap2.States.Find(x => x.Name.Equals(county.Field<string>("State"))).AddCounty(new USCounty(county.Field<string>("County"), rand.NextDouble() / 3));
        //        }

        //    }

        //    //create a pie chart
        //    List<string> piePointNames = new List<string>(new string[] { "Obese", "Overweight", "Normal", "Underweight" });
        //    List<double> piePointValues = new List<double>(new double[] { 0.24, 0.36, 0.18, 0.12 });
        //    PieChart obesityPie = new PieChart("Obesity", piePointNames, piePointValues);

        //    CombinedData data = new CombinedData();
        //    data.GridData = testGrid;
        //    data.LineCharts.Add(testChart1);
        //    data.LineCharts.Add(testChart2);
        //    data.Maps.Add(testMap1);
        //    data.Maps.Add(testMap2);
        //    data.PieCharts.Add(obesityPie);

        //    //try to grab the comparison data from session
        //    var value = HttpContext.Session.GetString("CDCData");
        //    data.ComparisonData = value == null ? new ExternalDataController().GetExternalData() : JsonConvert.DeserializeObject<List<CDCData>>(value);
            
        //    return Json(data);
        //}

      

        /// <summary>
        /// Main function for queries requesting data.  Will call other data aggregators and return the full Json payload.
        /// </summary>
        /// <param name="ProviderID">The ID number of the group requesting the data</param>
        /// <param name="StartAge">Bottom of age range for patients being queried</param>
        /// <param name="EndAge">Top of age range for patients being queried</param>
        /// <param name="StartDate">Beginning of observation period being queried</param>
        /// <param name="EndDate">End of observation period being queried</param>
        /// <param name="Ethnicities">Ethnicities of subset to query</param>
        /// <param name="ZipCodes"></param>
        /// <param name="Counties"></param>
        /// <returns></returns>
        [HttpGet]
        public JsonResult GetData(int ProviderID = -1, int StartAge = 0, int EndAge = 150, string StartDate = "", string EndDate = "", string Gender = null, string Ethnicities = null, string ZipCodes = null, string Counties = null)
        {
            //create data do be merged
            CreateDataController cdc = new CreateDataController();
            CreatedDataContainer createdData = cdc.CreateData();

            //a list of pracitioners for file access
            List<Practitioner> practitioners = cdc.CreatePractitioner();
            //filter based on the provided ID
            Practitioner thisPractitioner = (practitioners.Where(x => x.ID == ProviderID).Count() > 0) ? practitioners.Where(x => x.ID == ProviderID).First() : new Practitioner(-1, "No Practitioner Selected - Full Data", true);


            //grab the bmi info for classification
            string bmiThresholdString = HttpContext.Session.GetString("ChildhoodBMI");
            DataTable bmiThresholds =  JsonConvert.DeserializeObject<DataTable>(bmiThresholdString);


            //Deserialize passed parameters
            List<string> ethnicityList = (Ethnicities == null) ? null : JsonConvert.DeserializeObject<List<string>>(Ethnicities).ConvertAll(x => x.ToLower());
            List<string> zipList = (ZipCodes == null) ? null : JsonConvert.DeserializeObject<List<string>>(ZipCodes).ConvertAll(x => x.ToLower());
            List<string> countiesList = (Counties == null) ? null : JsonConvert.DeserializeObject<List<string>>(Counties).ConvertAll(x => x.ToLower());
            List<string> gendersList = (Gender == null) ? null : JsonConvert.DeserializeObject<List<string>>(Gender).ConvertAll(x => x.ToLower());

            //determine the date range
            DateTime startAgeDate = new DateTime(Math.Min(DateTime.Now.Year, DateTime.Now.Year - StartAge), DateTime.Now.Month, DateTime.Now.Day);
            DateTime endAgeDate = new DateTime(Math.Min(DateTime.Now.Year, DateTime.Now.Year - EndAge - 1), DateTime.Now.Month, DateTime.Now.Day).AddDays(-1);  

            //get the relevant patients
            List<HL7.Patient> rawPatList;
            List<Patient> patientList = new List<Patient>();
            try
            {
                rawPatList = requestAllPatients(startAgeDate.ToString("yyyy-MM-dd"), endAgeDate.ToString("yyyy-MM-dd"));
            }
            catch(Exception ex) {
                rawPatList = new List<HL7.Patient>();
            }

            //merge the patient lists
            rawPatList.AddRange(createdData.Patients.Where(x => DateTime.Parse(x.BirthDate) <= startAgeDate && DateTime.Parse(x.BirthDate) >= endAgeDate));


            //add patients if they match our request parameters
            foreach (var pat in rawPatList)
            {
                //convert them to our patient objects
                Patient thisPatient = new Patient(pat);
                

                //check the parameters
                bool correctZip = (zipList == null || zipList.Contains("")) ? true : zipList.Contains(thisPatient.Addresses.Last().Zip.ToLower());
                bool correctCounty = (countiesList == null || countiesList.Contains("")) ? true : (thisPatient.Addresses.Last().County == null) ? false : countiesList.Contains(thisPatient.Addresses.Last().County.ToLower());
                bool correctEthnicity = (ethnicityList == null || ethnicityList.Contains("")) ? true : ethnicityList.Contains(thisPatient.Ethnicity.ToLower());
                bool correctGender = (gendersList == null || gendersList.Contains("")) ? true : gendersList.Contains(thisPatient.Sex.ToLower());
                bool correctProvider = (ProviderID < 0) ? true : thisPatient.PractitionerIDs.Contains(ProviderID);
                bool correctAge = thisPatient.Age() >= StartAge && thisPatient.Age() <= EndAge;

                //add if they match
                if (correctZip && correctCounty && correctEthnicity && correctGender && correctProvider && correctAge)
                {
                    //set the bmi catgory
                    thisPatient.SetBMIClass(bmiThresholds);

                    //add the patient to the list
                    patientList.Add(thisPatient);
                }
            }

            //get the observations related to the current members
            List<HL7.Observation> rawObsList = new List<HL7.Observation>();
            List<string> obsCodes = new List<string>(new string[] { bmiCode, hemoglobinCode, heightCode, weightCode});
            foreach (var obsCode in obsCodes)
            {
                List<HL7.Observation> obsSubset;
                try
                {
                    obsSubset = requestAllObservations(startAgeDate.ToString("yyyy-MM-dd"), endAgeDate.ToString("yyyy-MM-dd"), obsCode);
                    obsSubset = obsSubset.Where(x => x.Code.Coding[0].Code == obsCode).ToList();
                }
                catch (Exception ex)
                {
                    obsSubset = new List<HL7.Observation>();
                }
                
                rawObsList.AddRange(obsSubset);
            }

            //merge the observation list
            rawObsList.AddRange(createdData.Observations);

            
            //update the patients to reflect the observations
            foreach (var pat in patientList)
            {
                //get the observations related to this patient
                var patObs = rawObsList.Where(x => x.Id == pat.ID);

                foreach(var obs in patObs)
                {
                    var observationCode = obs.Code.Coding[0].Code;
                    HL7.Quantity valueQuantity = (HL7.Quantity) obs.Value;
                    DateTime effectiveDate = DateTime.Parse((string)((HL7.FhirDateTime) obs.Effective).ObjectValue);
                    //var value = obs.Value
                    if (observationCode == bmiCode)
                    {
                        pat.BMIs.Add(new BMI(effectiveDate, valueQuantity.Value.Value));
                    }
                    else if(observationCode == hemoglobinCode)
                    {
                        pat.Hemoglobins.Add(new Hemoglobin(effectiveDate, valueQuantity.Value.Value));
                    }
                    else if(observationCode == heightCode)
                    {
                        if (valueQuantity.Value.HasValue)
                        {
                            pat.Heights.Add(new Height(effectiveDate, valueQuantity.Value.Value));
                        }
                    }
                    else if(observationCode == weightCode)
                    {
                        if (valueQuantity.Value.HasValue)
                        {
                            pat.Weights.Add(new Weight(effectiveDate, valueQuantity.Value.Value));
                        }
                    }
                }
            }


            //create the return wrapper
            CombinedData data = new CombinedData();

            //add the patients to the grid
            data.GridData = new Grid(patientList);

            //add the practitioner Info
            data.Practitioner = thisPractitioner;

            //add pie charts
            data.PieCharts = GetHyperBMI(patientList);
                       
            TimeSpan timeDifference = endAgeDate.Subtract(startAgeDate);

            string period = "";
            if (timeDifference.Days >= 1826)
                period = "YEAR";
            else if (timeDifference.Days >= 730)
                period = "QUARTERLY";
            else
                period = "MONTHLY";


            //convert the passed date range to date times for the line chart range
            DateTime lineChartStart;
            DateTime lineChartEnd;
            bool startDateOkay = DateTime.TryParse(StartDate, out lineChartStart);
            bool endDateOkay = DateTime.TryParse(EndDate, out lineChartEnd);
            lineChartStart = (startDateOkay) ? lineChartStart : new DateTime(1950, 1, 1);
            lineChartEnd = (endDateOkay) ? lineChartEnd : DateTime.Now;

            //create the line chart data
            data.LineCharts.Add(GetBMILine(patientList, lineChartStart, lineChartEnd, period));
            data.LineCharts.Add(GetHeightLine(patientList, lineChartStart, lineChartEnd, period));
            data.LineCharts.Add(GetWeightLine(patientList, lineChartStart, lineChartEnd, period));
            data.LineCharts.Add(GetHemoglobinLine(patientList, lineChartStart, lineChartEnd, period));

            //create the state data
            data.Maps.Add(GetMapChart(ChartType.BMI, patientList));
            data.Maps.Add(GetMapChart(ChartType.Hemoglobin, patientList));

            //push the data to session so it can be exported if requested
            HttpContext.Session.SetString("ChartData", JsonConvert.SerializeObject(data));


            return Json(data);
        }

        public MapChart GetMapChart(ChartType chartType, List<Patient> patientList)
        {
            //add the external data
            //try to grab the comparison data from session
            var value = HttpContext.Session.GetString("CDCData");
            List<CDCData> cdcData = value == null ? new ExternalDataController().GetExternalData() : JsonConvert.DeserializeObject<List<CDCData>>(value);

            MapChart map = new MapChart(chartType.ToString());
            map.States = new List<USState>();
            

            //add the states for the patients
            foreach(var state in cdcData[0].States)
            {
                //select the patients from the state
                var statePatients = patientList.Where(x => x.Addresses.Last().State.ToLower() == state.Name.ToLower());

                var counties = statePatients.Select(x => x.Addresses.Last().County).Distinct();

                double stateValue = 0.0;
                int obsCount = 0;

                //get the stats
                foreach(var patient in statePatients)
                {
                    if (chartType == ChartType.BMI)
                    {
                        if (patient.BMIs.Count > 0)  {
                            stateValue += (double)patient.BMIs.Last().Measurement;
                            obsCount++;
                        }
                    }
                    else if (chartType == ChartType.Hemoglobin)
                    {
                        if (patient.Hemoglobins.Count > 0)
                        {
                            stateValue += (double)patient.Hemoglobins.Last().Measurement;
                            obsCount++;
                        }
                    }
                    else
                    {
                        stateValue += patient.HypertensionClass == HypertensionClassification.Diagnosed ? 1.0 : 0;
                        obsCount++;
                    }
                }

                stateValue = (obsCount == 0) ? 0 : stateValue / obsCount;


                List<USCounty> countyData = new List<USCounty>(); 

                //cycle through the counties
                foreach(var county in counties)
                {
                    var countyPatients = statePatients.Where(x => x.Addresses.Last().County.ToLower() == county.ToLower());
                    double countyValue = 0.0;
                    int countyObsCount = 0;

                    //get the stats
                    foreach (var patient in countyPatients)
                    {
                        if (chartType == ChartType.BMI)
                        {
                            if (patient.BMIs.Count > 0)
                            {
                                countyValue += (double)patient.BMIs.Last().Measurement;
                                countyObsCount++;
                            }
                        }
                        else if (chartType == ChartType.Hemoglobin)
                        {
                            if (patient.Hemoglobins.Count > 0)
                            {
                                countyValue += (double)patient.Hemoglobins.Last().Measurement;
                                countyObsCount++;
                            }
                        }
                        else
                        {
                            countyValue += patient.HypertensionClass == HypertensionClassification.Diagnosed ? 1.0 : 0;
                            countyObsCount++;
                        }
                    }

                    countyValue = (countyObsCount == 0) ? 0 : countyValue / countyObsCount;
                    countyData.Add(new USCounty(county, countyValue));
                }

                //
                List<CDCObservation> cdcCompare = (chartType == ChartType.BMI) ? state.DataPoints.ToList() : null; 

                map.States.Add(new USState(state.Name, stateValue, cdcCompare, countyData.OrderByDescending(x => x.Value).Take(3).ToList()));
            }


            ////get the cdc data if it exists
            //if (chartType == ChartType.BMI)
            //{
            //    map.CDCComparisonStates = cdcData.Where(x => x.DataType == "Obesity_Adult").First().States;
            //}
            //else if (chartType == ChartType.Hemoglobin)
            //{
            //    map.CDCComparisonStates = cdcData.Where(x => x.DataType == "Hypertension_Adult").First().States;
            //}

            return map;
        }

        /// <summary>
        /// Create a pie chart for the provided data type
        /// </summary>
        /// <param name="chartType"></param>
        /// <param name="patientList"></param>
        /// <returns></returns>
        public PieChart GetPieChart(ChartType chartType, List<Patient> patientList)
        {
            string chartName = chartType.ToString();
            List<string> names = new List<string>();
            List<double> values = new List<double>();

            int totalCount = patientList.Count();

            if(chartType == ChartType.BMI)
            {
                //get the bmi types
                foreach (BMIClassification bmiClass in Enum.GetValues(typeof(BMIClassification)))
                {
                    //get the patient count
                    values.Add(patientList.Where(x => x.BMIClass == bmiClass).Count() / totalCount);
                    names.Add(bmiClass.ToString());
                }
            }
            else if(chartType == ChartType.Hemoglobin)
            {
                //get the hemoglobin types
                foreach (HemoglobinClassification hemoClass in Enum.GetValues(typeof(HemoglobinClassification)))
                {
                    //get the patient count
                    values.Add(patientList.Where(x => x.HemoglobinClass == hemoClass).Count() / totalCount);
                    names.Add(hemoClass.ToString());
                }
            }
            else if (chartType == ChartType.Hypertension)
            {
                foreach (HypertensionClassification hyperClass in Enum.GetValues(typeof(HypertensionClassification)))
                {
                    //get the patient count
                    values.Add(patientList.Where(x => x.HypertensionClass == hyperClass).Count() / totalCount);
                    names.Add(hyperClass.ToString());
                }
            }


            return new PieChart(chartName, names, values);
        }
        
        /// <summary>
        /// Returns BMI Pie charts for patients with and without hypertension
        /// </summary>
        /// <param name="patientList"></param>
        /// <returns></returns>
        public List<PieChart> GetHyperBMI(List<Patient> patientList)
        {
            List<PieChart> pieCharts = new List<PieChart>();

            //select patients with hypertension
            List<Patient> hypertensionPat = patientList.Where(x => x.HypertensionClass == HypertensionClassification.Diagnosed).ToList();
            List<Patient> nonHyperPat = patientList.Where(x => x.HypertensionClass == HypertensionClassification.Normal).ToList();



            //get the bmi types
            List<string> hyperNames = new List<string>();
            List<double> hyperValues = new List<double>();



            foreach (BMIClassification bmiClass in Enum.GetValues(typeof(BMIClassification)))
            {
                //get the patient count
                if(hypertensionPat.Count == 0)
                {
                    hyperValues.Add(0);
                }
                else
                {
                    hyperValues.Add((double)hypertensionPat.Where(x => x.BMIClass == bmiClass).Count() / hypertensionPat.Count());
                }
                hyperNames.Add(bmiClass.ToString());
            }

            pieCharts.Add(new PieChart("Hypertension", hyperNames, hyperValues));


            //get the bmi types
            List<string> nonHyperNames = new List<string>();
            List<double> nonHyperValues = new List<double>();


            foreach (BMIClassification bmiClass in Enum.GetValues(typeof(BMIClassification)))
            {
                //get the patient count
                if (nonHyperPat.Count > 0)
                {
                    nonHyperValues.Add((double)nonHyperPat.Where(x => x.BMIClass == bmiClass).Count() / nonHyperPat.Count());
                }
                else{
                    nonHyperValues.Add(0);
                }
                nonHyperNames.Add(bmiClass.ToString());
            }

            pieCharts.Add(new PieChart("Non-Hypertension", nonHyperNames, nonHyperValues));



            return pieCharts;

        }


        /// <summary>
        /// Will pull only paitents with BMI results and return a line with those data points.
        /// </summary>
        /// <param name="PatientList">already filted patient list</param>
        /// <param name="period">Accepts QUARTER, MONTHLY, YEARLY</param>
        /// <returns></returns>
        public LineChart GetBMILine(List<Patient> PatientList, DateTime startdate, DateTime enddate, string period = "QUARTER")
        {
            LineChart testChart = new LineChart("series-1", "BMI");
            
            List<BMI> BMIs = new List<BMI>();

            foreach(var patient in PatientList)
            {
                BMIs.AddRange(patient.BMIs);
            }

            BMIs = BMIs.OrderBy(p => p.Date).ToList();

            if(BMIs.Count == 0)
            {
                return testChart;
            }

            startdate = (BMIs.First().Date > startdate) ? BMIs.First().Date : startdate;
            enddate = (BMIs.Last().Date < enddate) ? BMIs.Last().Date : enddate;
            List<BMI> DataBMI = new List<BMI>();
            
            List<BMI> tempBMIs = new List<BMI>();
            decimal total = 0;

            switch (period)
            {
                case "QUARTER":
                    int currentMonth = startdate.Month;
                    int addmonths = 0;
                    if (currentMonth % 3 == 1)
                        addmonths = 2;
                    else if (currentMonth % 3 == 2)
                        addmonths = 1;

                    tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month + addmonths, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));
                    
                    if (tempBMIs.Count != 0)
                        total = (from num in tempBMIs select num.Measurement).Sum() / tempBMIs.Count;
                    if (total != 0)
                        DataBMI.Add(new BMI(startdate, total));

                    startdate = startdate.AddMonths(3);                    
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    DateTime nextDate = startdate.AddMonths(3);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddMonths(3))
                    {
                        tempBMIs.Clear();
                        tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= nextDate));
                        total = 0;
                        if (tempBMIs.Count != 0)
                            total = (from num in tempBMIs select num.Measurement).Sum() / tempBMIs.Count;
                        if (total != 0)
                            DataBMI.Add(new BMI(startdate, total));
                        nextDate = startdate.AddMonths(3);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                case "MONTH":
                    
                    tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));
                    
                    if (tempBMIs.Count != 0)
                        total = (from num in tempBMIs select num.Measurement).Sum() / tempBMIs.Count;
                    if (total != 0)
                        DataBMI.Add(new BMI(startdate, total));

                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while (startdate.CompareTo(enddate) <= 0)
                    {
                        tempBMIs.Clear();
                        tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempBMIs.Count != 0)
                            total = (from num in tempBMIs select num.Measurement).Sum() / tempBMIs.Count;
                        if (total != 0)
                            DataBMI.Add(new BMI(startdate, total));
                        if (startdate.Year == 9999)
                            break;
                        startdate = startdate.AddMonths(1);
                    }
                    break;
                case "YEAR":
                    tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, 12, 31))));
                    
                    if (tempBMIs.Count != 0)
                        total = (from num in tempBMIs select num.Measurement).Sum() / tempBMIs.Count;

                    if (total != 0)
                        DataBMI.Add(new BMI(startdate, total));

                    startdate = startdate.AddYears(1);
                    startdate = new DateTime(startdate.Year, 1, 1);
                    DateTime endOfYear = new DateTime(startdate.Year, 12, 31);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddYears(1))
                    {
                        tempBMIs.Clear();
                        tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= endOfYear));
                        total = 0;
                        if (tempBMIs.Count != 0)
                            total = (from num in tempBMIs select num.Measurement).Sum() / tempBMIs.Count;
                        if (total != 0)
                            DataBMI.Add(new BMI(startdate, total));
                        endOfYear = startdate.AddYears(1);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                default:
                    //defaulting on months                    
                    
                    tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));
                    
                    if (tempBMIs.Count != 0)
                        total = (from num in tempBMIs select num.Measurement).Sum() / tempBMIs.Count;
                    if (total != 0)
                        DataBMI.Add(new BMI(startdate, total));
                    
                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while(startdate.CompareTo(enddate) <= 0)
                    {
                        tempBMIs.Clear();
                        tempBMIs.AddRange(BMIs.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempBMIs.Count != 0)
                            total = (from num in tempBMIs select num.Measurement).Sum()/tempBMIs.Count;
                        if(total != 0)
                            DataBMI.Add(new BMI(startdate, total));
                        
                        if (startdate.Year == 9999)
                            break;

                        startdate = startdate.AddMonths(1);
                    }
                    break;
            }

            foreach (var BMIpoint in DataBMI)
                testChart.AddDataPoint(BMIpoint.Date, Convert.ToDouble(BMIpoint.Measurement));

            return testChart;
        }
        
        public LineChart GetHeightLine(List<Patient> PatientList, DateTime startdate, DateTime enddate, string period = "QUARTER")
        {
            LineChart testChart = new LineChart("series-1", "Height");

            List<Height> Heights = new List<Height>();

            foreach (var patient in PatientList)
            {
                Heights.AddRange(patient.Heights);
            }

            Heights = Heights.OrderBy(p => p.Date).ToList();

            if (Heights.Count == 0)
            {
                return testChart;
            }

            startdate = (Heights.First().Date > startdate) ? Heights.First().Date : startdate;
            enddate = (Heights.Last().Date < enddate) ? Heights.Last().Date : enddate;
            List<Height> DataHeight = new List<Height>();

            List<Height> tempHeights = new List<Height>();
            decimal total = 0;

            switch (period)
            {
                case "QUARTER":
                    int currentMonth = startdate.Month;
                    int addmonths = 0;
                    if (currentMonth % 3 == 1)
                        addmonths = 2;
                    else if (currentMonth % 3 == 2)
                        addmonths = 1;

                    tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month + addmonths, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempHeights.Count != 0)
                        total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;
                    if (total != 0)
                        DataHeight.Add(new Height(startdate, total));

                    startdate = startdate.AddMonths(3);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    DateTime nextDate = startdate.AddMonths(3);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddMonths(3))
                    {
                        tempHeights.Clear();
                        tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= nextDate));
                        total = 0;
                        if (tempHeights.Count != 0)
                            total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;
                        if (total != 0)
                            DataHeight.Add(new Height(startdate, total));
                        nextDate = startdate.AddMonths(3);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                case "MONTH":

                    tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempHeights.Count != 0)
                        total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;
                    if (total != 0)
                        DataHeight.Add(new Height(startdate, total));

                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while (startdate.CompareTo(enddate) <= 0)
                    {
                        tempHeights.Clear();
                        tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempHeights.Count != 0)
                            total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;
                        if (total != 0)
                            DataHeight.Add(new Height(startdate, total));
                        if (startdate.Year == 9999)
                            break;
                        startdate = startdate.AddMonths(1);
                    }
                    break;
                case "YEAR":
                    tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, 12, 31))));

                    if (tempHeights.Count != 0)
                        total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;

                    if (total != 0)
                        DataHeight.Add(new Height(startdate, total));

                    startdate = startdate.AddYears(1);
                    startdate = new DateTime(startdate.Year, 1, 1);
                    DateTime endOfYear = new DateTime(startdate.Year, 12, 31);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddYears(1))
                    {
                        tempHeights.Clear();
                        tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= endOfYear));
                        total = 0;
                        if (tempHeights.Count != 0)
                            total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;
                        if (total != 0)
                            DataHeight.Add(new Height(startdate, total));
                        endOfYear = startdate.AddYears(1);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                default:
                    //defaulting on months                    

                    tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempHeights.Count != 0)
                        total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;
                    if (total != 0)
                        DataHeight.Add(new Height(startdate, total));

                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while (startdate.CompareTo(enddate) <= 0)
                    {
                        tempHeights.Clear();
                        tempHeights.AddRange(Heights.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempHeights.Count != 0)
                            total = (from num in tempHeights select num.Measurement).Sum() / tempHeights.Count;
                        if (total != 0)
                            DataHeight.Add(new Height(startdate, total));

                        if (startdate.Year == 9999)
                            break;

                        startdate = startdate.AddMonths(1);
                    }
                    break;
            }

            foreach (var Heightpoint in DataHeight)
                testChart.AddDataPoint(Heightpoint.Date, Convert.ToDouble(Heightpoint.Measurement));

            return testChart;
        }

        public LineChart GetWeightLine(List<Patient> PatientList, DateTime startdate, DateTime enddate, string period = "QUARTER")
        {
            LineChart testChart = new LineChart("series-1", "Weight");

            List<Weight> Weights = new List<Weight>();

            foreach (var patient in PatientList)
            {
                Weights.AddRange(patient.Weights);
            }

            Weights = Weights.OrderBy(p => p.Date).ToList();

            if (Weights.Count == 0)
            {
                return testChart;
            }

            startdate = (Weights.First().Date > startdate) ? Weights.First().Date : startdate;
            enddate = (Weights.Last().Date < enddate) ? Weights.Last().Date : enddate;
            List<Weight> DataWeight = new List<Weight>();

            List<Weight> tempWeights = new List<Weight>();
            decimal total = 0;

            switch (period)
            {
                case "QUARTER":
                    int currentMonth = startdate.Month;
                    int addmonths = 0;
                    if (currentMonth % 3 == 1)
                        addmonths = 2;
                    else if (currentMonth % 3 == 2)
                        addmonths = 1;

                    tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month + addmonths, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempWeights.Count != 0)
                        total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;
                    if (total != 0)
                        DataWeight.Add(new Weight(startdate, total));

                    startdate = startdate.AddMonths(3);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    DateTime nextDate = startdate.AddMonths(3);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddMonths(3))
                    {
                        tempWeights.Clear();
                        tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= nextDate));
                        total = 0;
                        if (tempWeights.Count != 0)
                            total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;
                        if (total != 0)
                            DataWeight.Add(new Weight(startdate, total));
                        nextDate = startdate.AddMonths(3);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                case "MONTH":

                    tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempWeights.Count != 0)
                        total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;
                    if (total != 0)
                        DataWeight.Add(new Weight(startdate, total));

                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while (startdate.CompareTo(enddate) <= 0)
                    {
                        tempWeights.Clear();
                        tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempWeights.Count != 0)
                            total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;
                        if (total != 0)
                            DataWeight.Add(new Weight(startdate, total));
                        if (startdate.Year == 9999)
                            break;
                        startdate = startdate.AddMonths(1);
                    }
                    break;
                case "YEAR":
                    tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, 12, 31))));

                    if (tempWeights.Count != 0)
                        total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;

                    if (total != 0)
                        DataWeight.Add(new Weight(startdate, total));

                    startdate = startdate.AddYears(1);
                    startdate = new DateTime(startdate.Year, 1, 1);
                    DateTime endOfYear = new DateTime(startdate.Year, 12, 31);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddYears(1))
                    {
                        tempWeights.Clear();
                        tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= endOfYear));
                        total = 0;
                        if (tempWeights.Count != 0)
                            total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;
                        if (total != 0)
                            DataWeight.Add(new Weight(startdate, total));
                        endOfYear = startdate.AddYears(1);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                default:
                    //defaulting on months                    

                    tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempWeights.Count != 0)
                        total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;
                    if (total != 0)
                        DataWeight.Add(new Weight(startdate, total));

                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while (startdate.CompareTo(enddate) <= 0)
                    {
                        tempWeights.Clear();
                        tempWeights.AddRange(Weights.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempWeights.Count != 0)
                            total = (from num in tempWeights select num.Measurement).Sum() / tempWeights.Count;
                        if (total != 0)
                            DataWeight.Add(new Weight(startdate, total));

                        if (startdate.Year == 9999)
                            break;

                        startdate = startdate.AddMonths(1);
                    }
                    break;
            }

            foreach (var Weightpoint in DataWeight)
                testChart.AddDataPoint(Weightpoint.Date, Convert.ToDouble(Weightpoint.Measurement));

            return testChart;
        }
        
        /// <summary>
        /// Creates a Hemoglobin Line Chart from a list of Patients with Observations
        /// </summary>
        /// <param name="PatientList"></param>
        /// <param name="startdate"></param>
        /// <param name="enddate"></param>
        /// <param name="period"></param>
        /// <returns></returns>
        public LineChart GetHemoglobinLine(List<Patient> PatientList, DateTime startdate, DateTime enddate, string period = "QUARTER")
        {

            LineChart testChart = new LineChart("series-1", "Hemoglobin");

            List<Hemoglobin> Hemoglobins = new List<Hemoglobin>();

            foreach (var patient in PatientList)
            {
                Hemoglobins.AddRange(patient.Hemoglobins);
            }

            Hemoglobins = Hemoglobins.OrderBy(p => p.Date).ToList();

            if (Hemoglobins.Count == 0)
            {
                return testChart;
            }

            startdate = (Hemoglobins.First().Date > startdate) ? Hemoglobins.First().Date : startdate;
            enddate = (Hemoglobins.Last().Date < enddate) ? Hemoglobins.Last().Date : enddate;
            List<Hemoglobin> DataHemoglobin = new List<Hemoglobin>();

            List<Hemoglobin> tempHemoglobins = new List<Hemoglobin>();
            decimal total = 0;

            switch (period)
            {
                case "QUARTER":
                    int currentMonth = startdate.Month;
                    int addmonths = 0;
                    if (currentMonth % 3 == 1)
                        addmonths = 2;
                    else if (currentMonth % 3 == 2)
                        addmonths = 1;

                    tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month + addmonths, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempHemoglobins.Count != 0)
                        total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;
                    if (total != 0)
                        DataHemoglobin.Add(new Hemoglobin(startdate, total));

                    startdate = startdate.AddMonths(3);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    DateTime nextDate = startdate.AddMonths(3);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddMonths(3))
                    {
                        tempHemoglobins.Clear();
                        tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= nextDate));
                        total = 0;
                        if (tempHemoglobins.Count != 0)
                            total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;
                        if (total != 0)
                            DataHemoglobin.Add(new Hemoglobin(startdate, total));
                        nextDate = startdate.AddMonths(3);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                case "MONTH":

                    tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempHemoglobins.Count != 0)
                        total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;
                    if (total != 0)
                        DataHemoglobin.Add(new Hemoglobin(startdate, total));

                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while (startdate.CompareTo(enddate) <= 0)
                    {
                        tempHemoglobins.Clear();
                        tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempHemoglobins.Count != 0)
                            total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;
                        if (total != 0)
                            DataHemoglobin.Add(new Hemoglobin(startdate, total));
                        if (startdate.Year == 9999)
                            break;
                        startdate = startdate.AddMonths(1);
                    }
                    break;
                case "YEAR":
                    tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, 12, 31))));

                    if (tempHemoglobins.Count != 0)
                        total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;

                    if (total != 0)
                        DataHemoglobin.Add(new Hemoglobin(startdate, total));

                    startdate = startdate.AddYears(1);
                    startdate = new DateTime(startdate.Year, 1, 1);
                    DateTime endOfYear = new DateTime(startdate.Year, 12, 31);
                    for (; startdate.CompareTo(enddate) <= 0; startdate = startdate.AddYears(1))
                    {
                        tempHemoglobins.Clear();
                        tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= endOfYear));
                        total = 0;
                        if (tempHemoglobins.Count != 0)
                            total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;
                        if (total != 0)
                            DataHemoglobin.Add(new Hemoglobin(startdate, total));
                        endOfYear = startdate.AddYears(1);
                        if (startdate.Year == 9999)
                            break;
                    }
                    break;
                default:
                    //defaulting on months                    

                    tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= (new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month)))));

                    if (tempHemoglobins.Count != 0)
                        total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;
                    if (total != 0)
                        DataHemoglobin.Add(new Hemoglobin(startdate, total));

                    startdate = startdate.AddMonths(1);
                    startdate = new DateTime(startdate.Year, startdate.Month, 1);
                    while (startdate.CompareTo(enddate) <= 0)
                    {
                        tempHemoglobins.Clear();
                        tempHemoglobins.AddRange(Hemoglobins.Where(p => p.Date >= startdate && p.Date <= new DateTime(startdate.Year, startdate.Month, DateTime.DaysInMonth(startdate.Year, startdate.Month))));
                        total = 0;
                        if (tempHemoglobins.Count != 0)
                            total = (from num in tempHemoglobins select num.Measurement).Sum() / tempHemoglobins.Count;
                        if (total != 0)
                            DataHemoglobin.Add(new Hemoglobin(startdate, total));

                        if (startdate.Year == 9999)
                            break;

                        startdate = startdate.AddMonths(1);
                    }
                    break;
            }

            foreach (var Hemoglobinpoint in DataHemoglobin)
                testChart.AddDataPoint(Hemoglobinpoint.Date, Convert.ToDouble(Hemoglobinpoint.Measurement));

            return testChart;
        }

        // GET: /<controller>/
        public IActionResult Index()
        {
            //CreateDataController cdc = new CreateDataController();
            ExternalDataController exDC = new ExternalDataController();

            //read in external CDC data and save it to session
            List<CDCData> externalData = exDC.GetExternalData();
            HttpContext.Session.SetString("CDCData", JsonConvert.SerializeObject(externalData));

            //read in BMI tables for children 2-20
            DataTable childhoodBMI = exDC.ReadChildhoodBMI();
            HttpContext.Session.SetString("ChildhoodBMI", JsonConvert.SerializeObject(childhoodBMI));

            return View();
        }

        public void toTextTest()
        {
            string path = @"your path here";
            // Create a file to write to. 
            var observations = requestAllObservations();
            string text = "";
            string totaltext = "";
            foreach (var observation in observations)
            {
                text = observation.Subject.Url.ToString();
                text = text.Substring(8, text.Length -8);
                text += "~";
                text += observation.Code.Coding[0].Display;
                text += "~";
                text += observation.Effective.ToString();
                text += "~";
                //ObjectValue	"Blood pressure 146/93 mmHg"	object {string}
                if(observation.Value.ToString().Contains("Blood pressure"))
                {
                    string temp = observation.Value.ToString();
                    int start = temp.IndexOf("pressure")+8;
                    int finish = temp.IndexOf("mm")-start;
                    text += temp.Substring(start, finish).Trim();
                }
                else
                {
                    text += ((HL7.SimpleQuantity)observation.Value).Value;
                }
                
                totaltext += text + Environment.NewLine;
            }
            
            System.IO.File.WriteAllText(path, totaltext);
            
        }

    }
}
