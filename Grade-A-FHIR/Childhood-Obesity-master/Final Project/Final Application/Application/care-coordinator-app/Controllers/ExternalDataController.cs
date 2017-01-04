using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNet.Mvc;
using Hl7.Fhir.Rest;
using HL7 = Hl7.Fhir.Model;
using LumenWorks.Framework.IO.Csv;
using System.IO;
using System.Data;
using CDC_Obesity_App.Models;

namespace CDC_Obesity_App.Controllers
{
    public class ExternalDataController : Controller
    {

        /// <summary>
        /// reads the obesity and hypertension data from CDC
        /// </summary>
        /// <returns></returns>
        public List<CDCData> GetExternalData()
        {
            DataTable rawObesityData = ReadCDCData("cdc_obesity_data.csv");
            DataTable rawHypertensionData = ReadCDCData("cdc_hypertension_data.csv");

            List<CDCData> returnData = new List<CDCData>();
            returnData.Add(ConvertDataTable("Obesity_Adult", rawObesityData));
            returnData.Add(ConvertDataTable("Hypertension_Adult", rawHypertensionData));

            return returnData;
        }

        /// <summary>
        /// Converts a data table of CDC data to a CDCData object
        /// </summary>
        /// <param name="dataType">Name of data being read</param>
        /// <param name="dataTable"></param>
        /// <returns></returns>
        public CDCData ConvertDataTable(string dataType, DataTable dataTable)
        {
            //get all the states
            DataTable states = dataTable.DefaultView.ToTable(true, "Location");
            List<CDCState> cdcStateData = new List<CDCState>();

            int year = Int32.Parse(dataTable.Rows[0].Field<string>("Year"));

            foreach (DataRow state in states.Rows)
            {
                //get a list of observation types
                List<DataRow> observations =
                    (from obs in dataTable.AsEnumerable()
                     where obs.Field<string>("Location").Equals(state.Field<string>("Location"))
                     select obs).ToList<DataRow>();

                
                List<double> values = new List<double>();
                List<string> names = new List<string>();
                foreach (var obs in observations) {
                    values.Add(Double.Parse(obs.Field<string>("Data_Value")));
                    names.Add(obs.Field<string>("Response"));
                }

                cdcStateData.Add(new CDCState(state.Field<string>("Location"), names, values));
            }

            CDCData data = new CDCData(dataType, year, cdcStateData);

            return data;
        }


        /// <summary>
        /// Reads in CDC comparison data 
        /// </summary>
        /// <param name="filename"></param>
        /// <returns></returns>
        public DataTable ReadCDCData(string filename)
        {
            DataTable dt = new DataTable();
            dt.Columns.Add("Year");
            dt.Columns.Add("Location");
            dt.Columns.Add("Response");
            dt.Columns.Add("Data_Value");

            using (TextReader reader = System.IO.File.OpenText(filename))
            {
                CsvReader csvReader = new CsvReader(reader, true);

                while (csvReader.ReadNextRecord())
                {
                    DataRow dr = dt.NewRow();
                    dr["Year"] = csvReader["Year"];
                    dr["Location"] = csvReader["LocationDesc"];
                    dr["Response"] = csvReader["Response"];
                    dr["Data_Value"] = csvReader["Data_Value"];

                    dt.Rows.Add(dr);
                }
            }

            return dt;
        }

        /// <summary>
        /// creates a data table of bmi thresholds for children 2-20
        /// </summary>
        /// <returns></returns>
        public DataTable ReadChildhoodBMI()
        {
            DataTable dt = new DataTable();
            dt.Columns.Add("age_in_months");
            dt.Columns.Add("gender");
            dt.Columns.Add("bmi_percentile");
            dt.Columns.Add("bmi");

            using (TextReader reader = System.IO.File.OpenText("child_bmi_thesholds.csv"))
            {
                CsvReader csvReader = new CsvReader(reader, true);

                while (csvReader.ReadNextRecord())
                {
                    DataRow dr = dt.NewRow();
                    dr["age_in_months"] = csvReader["age_in_months"];
                    dr["gender"] = csvReader["gender"];
                    dr["bmi_percentile"] = csvReader["bmi_percentile"];
                    dr["bmi"] = csvReader["bmi"];

                    dt.Rows.Add(dr);
                }
            }

            return dt;
        }
    }
}
