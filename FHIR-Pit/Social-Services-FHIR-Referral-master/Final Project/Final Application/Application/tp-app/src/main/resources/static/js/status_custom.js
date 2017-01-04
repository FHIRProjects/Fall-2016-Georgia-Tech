$(function() {
	
    function getStatus(){
    	$.get( "http://localhost:8081/getAllReferral", function( data ) {
    	$('.service-status').html("");
		 for(var i = 0; i < data.length; i++) {			 
			 $('.service-status').append('<div class="col-lg-3"><div class="well well-lg service-well"><div class="service-image"><img src="/images/es.png"></div><div><label>Patient Referral :</label><span> ' + data[i].name + '</span></br><label>referalId :</label><span> ' + data[i].referalId + '</span></br><label>gender :</label><span> ' + data[i].gender + '</span></br><label>service :</label><span> ' + data[i].service + '</span></br><label>DOB :</label><span> ' + data[i].dob + '</span></br><label>contactInfo :</label><span class="' + data[i].contactInfo + '">' + data[i].address + '</span></div><div class="credentials_'+i+'"></div><button class="btn btn-info delete-service" data-id="'+data[i].referalId+'">Approve</button></div></div>');
			 
			 var credentials =  data[i].credentials;
			 
//			 for(var j = 0; j < credentials.length; j++) {
//				 
//				 $('.credentials_'+i).append('<hr><label>'+credentials[j].desc+': </label><span> ' + credentials[j].ip + '</span></br><div class="username_'+i+'"></div>');
//     
//				 if(credentials[j].options["Password"] != undefined)
//				 {
//					 $('.username_'+i).append('<label>Username : </label><span> ' + credentials[j].options["username"] + '</span></br><label>Password : </label><span> ' + credentials[j].options["Password"] + '</span></br>');
//					 
//				 }
//				 
//			 }
			 
		  }
		 
	   });
    
    }
    
    getStatus();
    
    setInterval(function(){		
		getStatus();	    	
	}, 5000);
    
    var refid ="";
    $(document).on("click",".delete-service", function(){     
    	refid = $(this).attr('data-id');       
        $('#deleteModal h5.modal-body-txt').html('Are you sure you want to approve this request: "' + refid + '" ?');
        $('#deleteModal').modal('show');
        
       }); 
    
    
    $('#deleteService').click(function(event) {     	
    	$.ajax({
        type:"PUT",
        url:"http://localhost:8080/updateStatus/"+refid+"?status=approved",
        error:  function(response){
      	  $('#errProcessModal').modal('show');
        },
        success:function(response){
      	  location.reload();
        }
    });
        
   });
    
});
