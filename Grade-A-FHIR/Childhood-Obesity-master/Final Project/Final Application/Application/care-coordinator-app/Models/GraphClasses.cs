using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CDC_Obesity_App.Models
{
    /// <summary>
    /// Total payload for front end
    /// </summary>
    public class CombinedData
    {
        public Practitioner Practitioner { get; set; }
        public Grid GridData { get; set; }
        public List<LineChart> LineCharts = new List<LineChart>();
        public List<MapChart> Maps = new List<MapChart>();
        public List<PieChart> PieCharts = new List<PieChart>();
        public List<CDCData> ComparisonData = new List<CDCData>();

    }

    public class Practitioner
    {
        public int ID { get; set; }
        public string Name { get; set; }
        public bool ShowPatientNames { get; set; }

        public Practitioner(){
        }

        public Practitioner(int id, string name, bool showPatientNames)
        {
            ID = id;
            Name = name;
            ShowPatientNames = showPatientNames;
        }
    }

    /// <summary>
    /// Individual user data in grid form
    /// </summary>
    public class Grid
    {
        public List<GridRow> rows = new List<GridRow>();

        public Grid()
        {

        }

        public Grid(List<Patient> patientList)
        {
            foreach(var patient in patientList)
            {
                string weight = (patient.Weights.Count == 0) ? "" : Math.Round(patient.Weights.Last().Measurement, 2).ToString();
                string height = (patient.Heights.Count == 0) ? "" : Math.Round(patient.Heights.Last().Measurement, 2).ToString();
                string bmi = (patient.BMIs.Count == 0) ? "" : Math.Round(patient.BMIs.Last().Measurement, 2).ToString();
                string hemoglobin = (patient.Hemoglobins.Count == 0) ? "" : Math.Round(patient.Hemoglobins.Last().Measurement, 2).ToString();

                AddRow(patient.ID, patient.FirstName, patient.LastName, patient.Age(), patient.Sex, patient.Ethnicity, height, weight, bmi, hemoglobin, patient.HypertensionClass.ToString());
            }
        } 

        public void AddRow(string id, string firstName, string lastName, int age, string gender, string race, string height, string weight, string bmi, string hemoglobin, string hypertension)
        {
            rows.Add(new GridRow(id, firstName, lastName, age, gender, race, height, weight, bmi, hemoglobin, hypertension));
        }
    }

    /// <summary>
    /// Rows for the grid 
    /// </summary>
    public class GridRow
    {
        public string ID { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public int Age { get; set; }
        public string Gender { get; set; }
        public string Race { get; set; }
        public string Height { get; set; }
        public string Weight { get; set; }
        public string BMI { get; set; }
        public string Hemoglobin { get; set; }
        public string Hypertension { get; set; }

        public GridRow()
        {

        }

        public GridRow(string id, string firstName, string lastName, int age, string gender, string race, string height, string weight, string bmi, string hemoglobin, string hypertension)
        {
            ID= id;
            FirstName = firstName;
            LastName = lastName;
            Age = age;
            Gender = gender;
            Race = race;
            Height = height;
            Weight = weight;
            BMI = bmi;
            Hemoglobin = hemoglobin;
            Hypertension = hypertension;
        }
    }

    /// <summary>
    /// Data structure representing Line charts
    /// </summary>
    public class LineChart
    {
        public string ID { get; set; }
        public string Name { get; set; }
        public List<LineChartPoints> Data = new List<LineChartPoints>();

        public LineChart()
        {

        }

        public LineChart(string id, string name)
        {
            ID = id;
            Name = name;
        }

        public void AddDataPoint(DateTime date, double value)
        {
            Data.Add(new LineChartPoints(date, value));
            Data = Data.OrderBy(p => p.Date).ToList();
        }
    }

    /// <summary>
    /// Individual points for a line chart
    /// </summary>
    public class LineChartPoints
    {
        public DateTime Date { get; set; }
        public double Value { get; set; }

        public LineChartPoints()
        {

        }

        public LineChartPoints(DateTime date, double value)
        {
            Date = date;
            Value = value;
        }
    }

    /// <summary>
    /// Data structure representing bar charts
    /// </summary>
    public class BarChart
    {
        public string ID { get; set; }
        public string Name { get; set; }
        public List<BarChartPoints> Data = new List<BarChartPoints>();

        public BarChart()
        {

        }

        public BarChart(string id, string name)
        {
            ID = id;
            Name = name;
        }

        public void AddDataPoint(string name, double value)
        {
            Data.Add(new BarChartPoints(name, value));
        }
    }

    /// <summary>
    /// Individual points for a bar chart
    /// </summary>
    public class BarChartPoints
    {
        public string Name { get; set; }
        public double Value { get; set; }

        public BarChartPoints()
        {

        }

        public BarChartPoints(string name, double value)
        {
            Name = name;
            Value = value;
        }
    }

    public class MapChart
    {
        public List<USState> States { get; set; }
        //public List<CDCState> CDCComparisonStates { get; set; }
        public string MapType { get; set; }

        public MapChart()
        {

        }

        /// <summary>
        /// Creates state collection with 0 value
        /// </summary>
        /// <param name="mapType">Observation being graphed</param>
        public MapChart(string mapType)
        {
            //names used for Highchart 
            string[] StateNames =  {"Massachusetts", "Washington", "California", "Oregon", "Wisconsin", "Maine", "Michigan", "Nevada", "New Mexico",
                "Colorado", "Wyoming", "Kansas", "Nebraska", "Oklahoma", "Missouri", "Illinois", "Indiana", "Vermont", "Arkansas", "Texas", "Rhode Island",
                "Alabama", "Mississippi", "North Carolina", "Virginia", "Iowa", "Maryland", "Delaware", "Pennsylvania", "New Jersey", "New York", "Idaho",
                "South Dakota", "Connecticut", "New Hampshire", "Kentucky", "Ohio", "Tennessee", "West Virginia", "District of Columbia", "Louisiana", "Florida",
                "Georgia", "South Carolina", "Minnesota", "Montana", "North Dakota", "Arizona", "Utah", "Hawaii", "Alaska" };

            MapType = mapType;
            States = new List<USState>();

            foreach(var state in StateNames)
            {
                States.Add(new USState(state, 0));
            }
        }

        /// <summary>
        /// Creates map object using provided state list
        /// </summary>
        /// <param name="mapType">Observation being graphed</param>
        /// <param name="states">List of state objects</param>
        public MapChart(string mapType, List<USState> states)
        {
            MapType = mapType;
            States = states;
        }

        /// <summary>
        /// Update the value of the given state
        /// </summary>
        /// <param name="stateName">Name of the state to update</param>
        /// <param name="value">Update value</param>
        /// <returns>True if update successful</returns>
        public bool UpdateState(string stateName, double value)
        {
            var updatedState = States.Where(a => a.Name == stateName);

            //if no state with that name found
            if(updatedState == null)
            {
                return false;
            }

            updatedState.First().Update(value);

            return true;
        }
    }

    public class USState
    {
        public string Name { get; set;}
        public double Value { get; set; }
        public List<CDCObservation> CDCValue { get; set; }
        public List<USCounty> Counties { get; set; }

        public USState()
        {

        }

        public USState(string name, double value, List<CDCObservation> cdcValue = null, List<USCounty> counties = null)
        {
            Name = name;
            Value = value;
            CDCValue = cdcValue;
            Counties = counties;
        }

        public void AddCounties(List<USCounty> counties)
        {
            Counties = counties;
        }

        public void AddCounty(USCounty county)
        {
            if(Counties == null)
            {
                Counties = new List<USCounty>();
            }

            int index = Counties.FindIndex(x => x.Name.Equals(county.Name));

            if(index < 0)
            {
                Counties.Add(county);
            }
            else
            {
                Counties[index].Update(county.Value);
            }
        }

        public void Update(double value)
        {
            Value = value;
        }
    }

    public class USCounty
    {
        public string Name { get; set; }
        public double Value { get; set; }

        public USCounty()
        {

        }

        public USCounty(string name, double value)
        {
            Name = name;
            Value = value;
        }

        public void Update(double value)
        {
            Value = value;
        }
    }

    public class PieChart
    {
        public string ChartType { get; set; }
        public List<PiePoint> DataPoints = new List<PiePoint>();

        public PieChart()
        {

        }

        public PieChart(string chartType, List<string> names, List<double> values)
        {
            ChartType = chartType;

            if(names.Count != values.Count)
            {
                throw new ArgumentException("List sizes must match");
            }

            for(int i=0; i<names.Count; i++)
            {
                DataPoints.Add(new PiePoint(names[i], values[i]));
            }
        }
    }

    public class PiePoint
    {
        public string Name { get; set; }
        public double Value { get; set; }

        public PiePoint()
        {

        }

        public PiePoint(string name, double value)
        {
            Name = name;
            Value = value;
        }
    }

    public enum ChartType
    {
        BMI,
        Hemoglobin,
        Hypertension
    }
}
