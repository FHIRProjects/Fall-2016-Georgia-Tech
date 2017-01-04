using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CDC_Obesity_App.Models
{
    public class Address
    {
        /// <summary>
        /// Address information
        /// </summary>
        public string Street { get; set; }
        public string City { get; set; }
        public string State { get; set; }
        public string Zip { get; set; }
        public string County { get; set; }
    }
}
