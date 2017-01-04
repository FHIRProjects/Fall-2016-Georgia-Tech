$(function() {

    $('#side-menu').metisMenu();

    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse');
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse');
        }

        height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height-10) + "px");
        }
    });

    var url = window.location;
    var element = $('ul.nav a').filter(function() {
        return this.href == url || url.href.indexOf(this.href) == 0;
    }).addClass('active').parent().parent().addClass('in').parent();
    if (element.is('li')) {
        element.addClass('active');
    }
    
    
    $('.service-dialog').click(function(event) { 
       localStorage.setItem("serviceName",$(this).attr('data-seviceName'));
       $('#launchServiceModal').modal('show');
       
    });
    
    $('#serviceSubmit').click(function(event) {
      $.ajax({
          type:"POST",
          url:"http://10.171.49.142:8085/createService",
          data:{"appName":localStorage.getItem("serviceName"),"userName":$('.remoteUser').html(),"validity":$('#validitySelect').val()},
          dataType: "JSON",
          error:  function(response){
        	  $('#errProcessModal').modal('show');
          },
          success:function(response){
        	  console.log(response);
        	  $('#successModal h5').html(response.msg);  
        	  $('#successModal h5').append("<a href='/status'>&nbsp;&nbsp;&nbsp; Click Here</a>");     
              $('#successModal').modal('show');
          }
      });
        
     });

});
