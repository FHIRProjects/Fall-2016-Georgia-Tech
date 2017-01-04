using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using HL7 = Hl7.Fhir.Model;

using System.Data;
using Microsoft.AspNet.Http;

namespace CDC_Obesity_App.Models
{
    public class Patient
    {
        string[] ethnicities = new string[] { "Black", "Caucasian", "Hispanic"};

        public Patient(HL7.Patient patient)
        {
            Random rand = new Random();
            ID = patient.Id;
            FirstName = patient.Name.ElementAt(0).Given.ElementAt(0);
            LastName = patient.Name.ElementAt(0).Family.ElementAt(0);
            MiddleName = "";
            Sex = patient.GenderElement.Value.ToString();
            Ethnicity = ethnicities[rand.Next(0, ethnicities.Length)];
            Insurance_Type = "Unknown";
            Date_Of_Birth = Convert.ToDateTime(patient.BirthDate);
            Add_Address(patient.Address.ElementAt(0).Line.ElementAt(0), patient.Address.ElementAt(0).City, patient.Address.ElementAt(0).State, patient.Address.ElementAt(0).PostalCode, patient.Address.ElementAt(0).District);

            //if there isn't a saved practitioner generate and add it to the list, otherwise uses the provided one
            if(patient.CareProvider.Count == 0)
            {
                PractitionerIDs.Add(rand.Next(1, 5));
            }
            else
            {
                foreach(var practitionerRef in patient.CareProvider)
                {
                    PractitionerIDs.Add(Int32.Parse(practitionerRef.Reference.Replace("Practitioner/", "")));
                }
            }

            //default hypertension
            Has_HyperTension = false;
            HypertensionClass = HypertensionClassification.Normal;

            //default bmi
            BMIClass = BMIClassification.Normal;

            foreach (Data_Holder dh in Patient_Data.PatientInfo(ID))
            {
                switch (dh.Type)
                {
                    case "BMI":
                        BMIs.Add(new BMI(dh.Date, dh.Measurement));
                        break;
                    case "WEIGHT":
                        Weights.Add(new Weight(dh.Date, dh.Measurement));
                        break;
                    case "HEIGHT":
                        Heights.Add(new Height(dh.Date, dh.Measurement));
                        break;
                    case "HEMOGLOBIN":
                        Hemoglobins.Add(new Hemoglobin(dh.Date, dh.Measurement));
                        HemoglobinClass = (dh.Measurement < (decimal) 0.057) ? HemoglobinClassification.Normal : HemoglobinClassification.High;
                        break;
                    case "HYPERTENSION":
                        Has_HyperTension = true;
                        HypertensionClass = HypertensionClassification.Diagnosed;
                        break;
                }
            }
        }

        public Patient(){ }

        public string ID { get; private set; }

        public string FirstName { get; set; }
        
        public string LastName { get; set; }

        public string MiddleName { get; set; }

        public string Sex { get; set; }

        public string Ethnicity { get; private set; }

        public string Insurance_Type { get; set; }

        public List<int> PractitionerIDs = new List<int>();

        public DateTime Date_Of_Birth { get; private set; }

        public bool Has_HyperTension { get; private set; }

        public int Age()
        {
            if (Date_Of_Birth != null)
            {
                return (int)DateTime.Now.Subtract(Date_Of_Birth).TotalDays/365;
            }
            else
            {
                return 0;
            }
        }

        public void Add_Address(string street, string city, string state, string zip, string county = "")
        {
            Addresses.Add(new Address() { Street = street, City = city, State = state, Zip = zip, County = county });
        }

        public List<Address> Addresses = new List<Address>();

        private void FillObservations()
        {
            List<HL7.Observation> observations = (new CDC_Obesity_App.Controllers.HomeController()).requestPatientObservations(ID);
            
            foreach(var observation in observations)
            {
                if(observation.TypeName.ToUpper().Contains("HEIGHT"))
                {
                    this.Heights.Add(new Height(Convert.ToDateTime(observation.Effective.ToString()), (decimal)((HL7.SimpleQuantity)observation.Value).Value));
                }
                else if (observation.TypeName.ToUpper().Contains("WEIGHT"))
                {
                    this.Weights.Add(new Weight(Convert.ToDateTime(observation.Effective.ToString()), (decimal)((HL7.SimpleQuantity)observation.Value).Value));
                }
                else if (observation.TypeName.ToUpper().Contains("BMI"))
                {
                    this.BMIs.Add(new BMI(Convert.ToDateTime(observation.Effective.ToString()), (decimal)((HL7.SimpleQuantity)observation.Value).Value));
                }
                else if (observation.TypeName.ToUpper().Contains("HEMOGLOBIN"))
                {
                    this.Hemoglobins.Add(new Hemoglobin(Convert.ToDateTime(observation.Effective.ToString()), (decimal)((HL7.SimpleQuantity)observation.Value).Value));
                }
            }
            
        }

        private void FillConditions()
        {
            List<HL7.Condition> conditions = (new CDC_Obesity_App.Controllers.HomeController()).requestPatientConditions(ID);

            foreach (var condition in conditions)
            {
                Counseling_Session.Add(new Counseling(Convert.ToDateTime(condition.DateRecorded), condition.Code.Text));
            }
        }

        public void SetBMIClass(DataTable bmiData)
        {
            //get the last bmi observation
            if(BMIs.Count() > 0 && (Sex.ToLower() == "male" || Sex.ToLower() == "female"))
            {

                //determine the age at the last observation
                var lastObs = BMIs.Last();
                double ageInMonths = (lastObs.Date.Subtract(Date_Of_Birth).TotalDays / 365.25) * 12;

                //if over 20 use standard bmi
                if(ageInMonths > 20 * 12)
                {
                    if(lastObs.Measurement <  (decimal)18.5)
                    {
                        BMIClass = BMIClassification.Underweight;
                    }
                    else if(lastObs.Measurement < (decimal)25.0)
                    {
                        BMIClass = BMIClassification.Normal;
                    }
                    else if (lastObs.Measurement < (decimal)25.0)
                    {
                        BMIClass = BMIClassification.Overweight;
                    }
                    else
                    {
                        BMIClass = BMIClassification.Obese;
                    }
                }
                else if(ageInMonths > 24)
                {
                    //otherwise use the childhood chart

                    //round the age in months to the nearest .5
                    ageInMonths = Math.Round(ageInMonths) + .5;

                    //select the data rows that are relevant
                    var results = from myRow in bmiData.AsEnumerable()
                                  where myRow.Field<string>("age_in_months") == ageInMonths.ToString() && Sex.ToLower() == myRow.Field<string>("gender")
                                  select myRow;

                    //if
                    var underweight = from row in results
                               where row.Field<string>("bmi_percentile") == "0.5"
                               select row;

                    var normal = from row in results
                                      where row.Field<string>("bmi_percentile") == "0.85"
                                      select row;

                    var overweight = from row in results
                                 where row.Field<string>("bmi_percentile") == "0.95"
                                 select row;


                    if (lastObs.Measurement < Decimal.Parse(underweight.First().Field<string>("bmi")))
                    {
                        BMIClass = BMIClassification.Underweight;

                    }
                    else if(lastObs.Measurement < Decimal.Parse(normal.First().Field<string>("bmi")))
                    {
                        BMIClass = BMIClassification.Normal;
                    }
                    else if (lastObs.Measurement < Decimal.Parse(overweight.First().Field<string>("bmi")))
                    {
                        BMIClass = BMIClassification.Overweight;
                    }
                    else
                    {
                        BMIClass = BMIClassification.Obese;
                    }
                }
                else
                {
                    BMIClass = BMIClassification.Normal;
                }
            }
            else
            {
                //if there are no observations just set it to normal
                BMIClass = BMIClassification.Normal;
            }

        }

        //BMI classification needs to be established
        public BMIClassification BMIClass;

        public HemoglobinClassification HemoglobinClass;

        public HypertensionClassification HypertensionClass;

        public List<Lab> LabTests = new List<Lab>();

        public List<Weight> Weights = new List<Weight>();

        public List<Height> Heights = new List<Height>();

        public List<Counseling> Counseling_Session = new List<Counseling>();

        public List<Patient> Children = new List<Patient>();

        public List<BMI> BMIs = new List<BMI>();

        public List<Hemoglobin> Hemoglobins = new List<Hemoglobin>();

        public decimal Current_Weight {
            get { return (from weight in Weights orderby weight.Date descending select weight.Measurement).FirstOrDefault(); }
        }

        public decimal Current_Height
        {
            get { return (from height in Heights orderby height.Date descending select height.Measurement).FirstOrDefault(); }
        }
        
    }

    public class Data_Holder
    {
        public string PatientID { get; set; }
        public string Type { get; set; }
        public DateTime Date { get; set; }
        public decimal Measurement { get; set; }
    }

    public static class Patient_Data
    {
        private static List<Data_Holder> All_Info;

         
        private static void  Fill_Patient_Data()
        {
            if(All_Info == null)
            {
                List<Data_Holder> templist = new List<Data_Holder>();
                using (StreamReader reader = new StreamReader("PatientInfo.txt"))
                {
                    string allLines = reader.ReadToEnd();
                    allLines = allLines.Replace('\r', ' ');
                    string[] seperateLines = allLines.Split('\n');
                    string PatientID = "";
                    foreach (string str in seperateLines)
                    {
                        int month, day, year;
                        string[] date;
                        string[] group = str.Split(';');
                        switch (group[0])
                        {
                            case "PATIENT":
                                PatientID = group[1].Trim();
                                break;
                            case "TYPE":
                                break;
                            case "HYPERTENSION":
                                date = group[1].Trim().Split('/');
                                month = Convert.ToInt32(date[0]);
                                day = Convert.ToInt32(date[1]);
                                year = Convert.ToInt32(date[2]);
                                templist.Add(new Data_Holder() { PatientID = PatientID, Type = group[0].Trim(), Date = new DateTime(year, month, day), Measurement = 1 });
                                break;
                            default:
                                date = group[1].Trim().Split('/');
                                month = Convert.ToInt32(date[0]);
                                day = Convert.ToInt32(date[1]);
                                year = Convert.ToInt32(date[2]);
                                templist.Add(new Data_Holder() { PatientID = PatientID, Type = group[0].Trim(), Date = new DateTime(year, month, day), Measurement = Convert.ToDecimal(group[2].Trim()) });
                                break;
                        }
                    }
                }
                All_Info = new List<Data_Holder>(templist);
            }
        }

        public static List<Data_Holder> PatientInfo(string patientID)
        {
            Fill_Patient_Data();
            return All_Info.Where(p => p.PatientID == patientID).ToList();
        }
    }

}
