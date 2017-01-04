using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNet.Mvc;
using System.Text;
using Microsoft.AspNet.Http;
using CDC_Obesity_App.Models;
using Newtonsoft.Json;
using System.Xml.Serialization;
using System.IO;
using System.Xml;


namespace CDC_Obesity_App.Controllers
{
    public class OutputController : Controller
    {
        /// <summary>
        /// Creates CSV for export
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
        [HttpGet]
        public FileContentResult DownloadCSV(CombinedData data = null)
        {

            CombinedData outputData = data;
            string CSVName = "ExportCSV.csv";


            if(data == null || (data.ComparisonData.Count == 0 && data.LineCharts.Count == 0 && data.Maps.Count == 0 && data.PieCharts.Count == 0 && data.GridData == null))
            {
                var value = HttpContext.Session.GetString("ChartData");
                outputData = value == null ? null : JsonConvert.DeserializeObject<CombinedData>(value);
            }

       
            if(outputData == null)
            {
                return File(new System.Text.UTF8Encoding().GetBytes(""), "text/csv", CSVName);
            }

            StringBuilder sb = new StringBuilder();

            sb = OutputPractitioner(sb, outputData.Practitioner);

            sb.Append(Environment.NewLine);
            sb.Append(Environment.NewLine);

            foreach (var lineChart in outputData.LineCharts)
            {
                sb = OutputLineChart(sb, lineChart);

                sb.Append(Environment.NewLine);
                sb.Append(Environment.NewLine);
            }

            foreach(var map in outputData.Maps)
            {
                sb = OutputMap(sb, map);

                sb.Append(Environment.NewLine);
                sb.Append(Environment.NewLine);
                
                //output the obesity comparison data
                if (map.MapType == ChartType.BMI.ToString())
                {
                    sb = OutputComparisonData(sb, map);
                    sb.Append(Environment.NewLine);
                    sb.Append(Environment.NewLine);
                }
            }

            


            foreach (var pie in outputData.PieCharts)
            {
                sb = OutputPieChart(sb, pie);

                sb.Append(Environment.NewLine);
                sb.Append(Environment.NewLine);
            }



            sb = OutputGrid(sb, outputData.GridData);

            


            return File(new System.Text.UTF8Encoding().GetBytes(sb.ToString()), "text/csv", CSVName);
        }

        /// <summary>
        /// Creates XML for export
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
        public FileResult ExportToXML(CombinedData data = null)
        {

            CombinedData outputData = data;
            string XMLName = "ExportXML.xml";


            if (data == null || (data.ComparisonData.Count == 0 && data.LineCharts.Count == 0 && data.Maps.Count == 0 && data.PieCharts.Count == 0 && data.GridData == null))
            {
                var value = HttpContext.Session.GetString("ChartData");
                outputData = value == null ? null : JsonConvert.DeserializeObject<CombinedData>(value);
            }

            if (outputData != null)
            {
                XmlSerializer xmlSerializer = new XmlSerializer(typeof(CombinedData));
                var subReq = new CombinedData();
                var stringWriter = new StringWriter();
                using (var writer = XmlWriter.Create(stringWriter))
                {
                    xmlSerializer.Serialize(writer, outputData);
                    return File(new System.Text.UTF8Encoding().GetBytes(stringWriter.ToString()), "text/xml", XMLName);
                }
            }

            return File(new System.Text.UTF8Encoding().GetBytes(""), "text/xml", XMLName);
        }

        /// <summary>
        /// Converts practioner class to string for CSV
        /// </summary>
        /// <param name="sb"></param>
        /// <param name="practitioner"></param>
        /// <returns></returns>
        private StringBuilder OutputPractitioner(StringBuilder sb, Practitioner practitioner)
        {
            sb.Append("Practitioner" + Environment.NewLine);
            sb.Append("Practitioner ID,");
            sb.Append(practitioner.ID + Environment.NewLine);
            sb.Append("Practitioner Name,");
            sb.Append(practitioner.Name + Environment.NewLine);

            return sb;
        }

        /// <summary>
        /// Converts grid class to string for csv
        /// </summary>
        /// <param name="sb"></param>
        /// <param name="grid"></param>
        /// <returns></returns>
        private StringBuilder OutputGrid(StringBuilder sb,  Grid grid)
        {
            sb.Append("Grid Data" + Environment.NewLine);
            sb.Append("ID, Last Name, First Name, Gender, Age, Race, Height, Weight, BMI, Hemoglobin, Hypertension" + Environment.NewLine);

            foreach(var row in grid.rows)
            {
                sb.Append(row.ID);
                sb.Append(",");
                sb.Append(row.LastName);
                sb.Append(",");
                sb.Append(row.FirstName);
                sb.Append(",");
                sb.Append(row.Gender);
                sb.Append(",");
                sb.Append(row.Age);
                sb.Append(",");
                sb.Append(row.Race);
                sb.Append(",");
                sb.Append(row.Height);
                sb.Append(",");
                sb.Append(row.Weight);
                sb.Append(",");
                sb.Append(row.BMI);
                sb.Append(",");
                sb.Append(row.Hemoglobin);
                sb.Append(",");
                sb.Append(row.Hypertension);
                sb.Append(Environment.NewLine);
            }

            return sb;
        }

        /// <summary>
        /// Converts line chart class to string for csv 
        /// </summary>
        /// <param name="sb"></param>
        /// <param name="lineChart"></param>
        /// <returns></returns>
        private StringBuilder OutputLineChart(StringBuilder sb, LineChart lineChart)
        {
            
            sb.Append("Line Chart: " + lineChart.Name + Environment.NewLine);
            sb.Append("Date, Value" + Environment.NewLine);

            foreach (var datapoint in lineChart.Data)
            {
                
                sb.Append(datapoint.Date);
                sb.Append(",");
                sb.Append(datapoint.Value);
                sb.Append(Environment.NewLine);
            }

            return sb;
        }

        /// <summary>
        /// Converts pie chart class to string for csv
        /// </summary>
        /// <param name="sb"></param>
        /// <param name="pieChart"></param>
        /// <returns></returns>
        private StringBuilder OutputPieChart(StringBuilder sb, PieChart pieChart)
        {

            sb.Append("Pie Chart: " + pieChart.ChartType + Environment.NewLine);
            sb.Append("Name, Value" + Environment.NewLine);

            foreach (var datapoint in pieChart.DataPoints)
            {

                sb.Append(datapoint.Name);
                sb.Append(",");
                sb.Append(datapoint.Value);
                sb.Append(Environment.NewLine);
            }

            return sb;
        }

        /// <summary>
        /// Converts map class to string for csv
        /// </summary>
        /// <param name="sb"></param>
        /// <param name="map"></param>
        /// <returns></returns>
        private StringBuilder OutputMap(StringBuilder sb, MapChart map)
        {
            sb.Append("Map: " + map.MapType + Environment.NewLine);
            sb.Append("State, Value" + Environment.NewLine);

            bool hasCounties = false;

            foreach (var state in map.States)
            {
                sb.Append(state.Name);
                sb.Append(",");
                sb.Append(state.Value);
                sb.Append(Environment.NewLine);

                if (state.Counties != null)
                {
                    hasCounties = true;
                }
            }

            if (hasCounties)
            {
                sb.Append(Environment.NewLine);
                sb.Append("County, State, Value" + Environment.NewLine);

                foreach (var state in map.States)
                {
                    if (state.Counties != null)
                    {
                        foreach (var county in state.Counties)
                        {
                            sb.Append(county.Name);
                            sb.Append(",");
                            sb.Append(state.Name);
                            sb.Append(",");
                            sb.Append(county.Value);
                            sb.Append(Environment.NewLine);
                        }
                    }
                }
            }


            return sb;
        }

        /// <summary>
        /// Converts CDC Data to string for csv
        /// </summary>
        /// <param name="sb"></param>
        /// <param name="map"></param>
        /// <returns></returns>
        private StringBuilder OutputComparisonData(StringBuilder sb, MapChart map)
        {

            sb.Append("CDC Data: " + map.MapType + Environment.NewLine);
            sb.Append("State, Category, Value" + Environment.NewLine);

            foreach (var state in map.States)
            {
                foreach (var obs in state.CDCValue)
                {

                    sb.Append(state.Name);
                    sb.Append(",");
                    sb.Append(obs.Name);
                    sb.Append(",");
                    sb.Append(obs.Value);
                    sb.Append(Environment.NewLine);
                }

            }

            return sb;
        }
    }
}
