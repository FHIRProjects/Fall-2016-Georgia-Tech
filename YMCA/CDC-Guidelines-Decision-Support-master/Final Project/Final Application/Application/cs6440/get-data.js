function getPatientName (pt) {
  if (pt.name) {
    var names = pt.name.map(function(name) {
      return name.given.join(" ") + " " + name.family.join(" ");
    });
    return names.join(" / ")
  } else {

    $('.chart').data('easyPieChart').update(0);
	$("#percentage").text('0%');
	$("#detect").text(' ');
	return "User not found.";
  }
}


function displayPatient (pt) {
  document.getElementById('output').innerHTML  = getPatientName(pt);
}

function callbackFunc(response) {
    // do something with the response
    var resp = JSON.parse(response);
    var prob = resp.prob;
    if(prob){
		 prob = prob * 100;
	}
	$('.chart').data('easyPieChart').update(prob);
	var prob1 = prob + '%';
	 $("#percentage").text(prob1);
    if (prob > 60 )
    {
      $("#detect").text('High Zika Probability Detected!! Please Please Follow CDC guidelines for the Patient');
    }


}

// Create a FHIR client (server URL, patient id in `demo`)

function displayPatientDetail(patientId) {
	$('.chart').data('easyPieChart').update(0);
	$("#detect").text(' ');
	$("#percentage").text('processing..');

	var demo = {
	    serviceUrl: "http://polaris.i3l.gatech.edu:8080/fhir-omopv5/base",
	    patientId: patientId
      };

	var smart = FHIR.client(demo),
	    pt = smart.patient;

	// Create a patient banner by fetching + rendering demographics
	smart.patient.read().then(function(pt) {
	  displayPatient (pt);
    })
    .fail(function() {
	   displayPatient ("");
    });
     var cond ="";
     var obs =""
    smart.patient.api.fetchAllWithReferences({type: "Condition"}).then(function(pt) {
      cond = pt;
    })
     smart.patient.api.fetchAllWithReferences({type: "Observation"}).then(function(pt) {
	  obs = pt;
	  var condJsonString = JSON.stringify(cond);
	  var obsJsonString = JSON.stringify(obs);
	  var condJson = JSON.parse(condJsonString);
	  var obsJson = JSON.parse(obsJsonString);
	  if(cond.length >0  || obs.length>0){
	   $.ajax({
	  	  type: "POST",
	  	  url: "http://192.168.1.17:5000/detect",
	  	  data: {mydata:JSON.stringify([{'observation': obsJson, 'condition': condJson}])},
	  	   success: function(returnValue){
					  console.log(returnValue);
					  callbackFunc(returnValue); //with return value excecute code

				  },
				  error: function(request,error) {
					  alert('An error occurred in server');
					  // console.log(request, error);
                }
          });
	    }
       })

}





