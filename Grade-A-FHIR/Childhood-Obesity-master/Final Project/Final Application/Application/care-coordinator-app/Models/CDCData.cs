using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CDC_Obesity_App.Models
{
    public class CDCData
    {
        public string DataType { get; set; }
        public List<CDCState> States { get; set; }
        public int Year { get; set; }

        public CDCData()
        {

        }

        public CDCData(string dataType, int year, List<CDCState> states)
        {
            DataType = dataType;
            Year = year;
            States = states;
        }
    }

    public class CDCState
    {
        public string Name { get; set; }
        public List<CDCObservation> DataPoints { get; set; }

        public CDCState()
        {

        }

        public CDCState(string stateName, List<string> dataPointNames, List<double> dataPointValues)
        {
            if (dataPointNames.Count != dataPointValues.Count)
            {
                throw new ArgumentException("List sizes must match");
            }

            Name = stateName;
            DataPoints = new List<CDCObservation>();

            for (int i = 0; i < dataPointNames.Count; i++)
            {
                DataPoints.Add(new CDCObservation(dataPointNames[i], dataPointValues[i]));
            }
        }
    }

    public class CDCObservation
    {
        public string Name { get; set; }
        public double Value { get; set; }

        public CDCObservation()
        {

        }

        public CDCObservation(string name, double value)
        {
            Name = name;
            Value = value;
        }
    }
}
