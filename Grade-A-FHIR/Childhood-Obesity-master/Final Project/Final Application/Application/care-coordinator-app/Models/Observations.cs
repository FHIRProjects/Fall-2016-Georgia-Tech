using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CDC_Obesity_App.Models
{
    public class Weight
    {
        public DateTime Date { get; private set; }
        public decimal Measurement { get; private set; }

        /// <summary>
        /// Describes patient weight observation at a given time 
        /// </summary>
        /// <param name="date">The date of the weight observation</param>
        /// <param name="measurement">The measured weight</param>
        public Weight(DateTime date, decimal measurement)
        {
            Date = date;
            Measurement = measurement;
        }
    }
    
    public class Height
    {
        public DateTime Date { get; private set; }
        public decimal Measurement { get; private set; }

        /// <summary>
        /// Describes patient height observation at a given time 
        /// </summary>
        /// <param name="date">The date of the height observation</param>
        /// <param name="measurement">The measured height</param>
        public Height(DateTime date, decimal measurement)
        {
            Date = date;
            Measurement = measurement;
        }
    }

    public class Counseling
    {
        public DateTime Date { get; private set; }
        public String CounselName { get; private set; }

        /// <summary>
        /// Describes the counseling performed at a given date
        /// </summary>
        /// <param name="date">The date of the counseling</param>
        /// <param name="counselName">Counseling type provided</param>
        public Counseling(DateTime date, string counselName)
        {
            Date = date;
            CounselName = counselName;
        }
    }

    public class Clinic
    {
        public DateTime Date { get; private set; }
        public String PatientID { get; private set; }

        /// <summary>
        /// The date of a clinic visit for a patient
        /// </summary>
        /// <param name="date">Date of the visit</param>
        /// <param name="patientID">The visiting patient's ID</param>
        public Clinic(DateTime date, string patientID)
        {
            Date = date;
            PatientID = patientID;
        }
    }

    public class Lab
    {
        /// <summary>
        /// Describes lab result for a patient 
        /// </summary>
        public string Lab_ID { get; private set; }
        public string Lab_Name { get; private set; }
        public DateTime Date { get; private set; }
        
        public List<string> Results { get; }

        public void Add_Result(string result)
        {
            Results.Add(result);
        }

        public void Add_Results(List<string> results)
        {
            Results.AddRange(results);
        }
    }
    
    /// <summary>
    /// Describes BMI results for a patient
    /// </summary>
    public class BMI
    {
        public DateTime Date { get; set; }
        public decimal Measurement { get; set; }
        public BMI(DateTime date, decimal measurement)
        {
            Date = date;
            Measurement = measurement;
        }
    }

    /// <summary>
    /// Describes Hemoglobin results for a patient
    /// </summary>
    public class Hemoglobin
    {
        public DateTime Date { get; set; }
        public decimal Measurement { get; set; }
        public Hemoglobin(DateTime date, decimal measurement)
        {
            Date = date;
            Measurement = measurement;
        }
    }

    /// <summary>
    /// BMI Category
    /// </summary>
    public enum BMIClassification
    {
        Underweight,
        Normal,
        Overweight,
        Obese
    }

    /// <summary>
    /// Hemoglobin Category
    /// </summary>
    public enum HemoglobinClassification
    {
        High,
        Normal
    }

    /// <summary>
    /// Hypertension Category
    /// </summary>
    public enum HypertensionClassification
    {
        Diagnosed,
        Normal
    }
}
