'use strict';
 
angular.module('Home')

.controller('HomeController', ['$scope','$http','$rootScope','$location','$cookies', function ($scope, $http, $rootScope, $location, $cookies) 
{
	$scope.orderByField = 'days';
	$scope.reverseSort = true;
	$scope.showing = false;
	
	$scope.init = function()
	{
		if ( !($cookies.get( 'username' ) === undefined ) )
		{
			if ($cookies.get( 'username' ) === '')
			{
				$location.path('/login');
				return;
			}
			else if ($cookies.get( 'username' ) === 'admin')
			{
				$location.path('/admin');
				return;
			}
		}
		
		$scope.dataLoading = true;		
		
		$http(
		{
			method: 'GET',
			url : '/cdc/api/stroke/patient',
			headers : 
			{
				'username' : $cookies.get( 'username' ), 
				'password' : $cookies.get( 'password' )
			}
		})
		.success(function (response)
		{	
			angular.forEach( response, function( value, key ) 
			{
				processDaysSinceDischarge( value );
				setQRavailable(value);
			});
			
			
			
			$scope.patients = response;
		});
	};
	
	var setQRavailable =  function (patient){
		
		if ((patient.questionnaireResponseId == null || patient.questionnaireResponseId.length < 1) && patient.days > 30){
			patient.displayQuestionnaire = '';
		}
	}
	
	$scope.viewQuestionnaire = function(patient)
	{
		$rootScope.selectedPatient = patient;
		$location.path('/questionnaire');
	};
	
	$scope.viewQuestionnaireResponse =  function(patient)
	{
		patient.qresponse =  patient.questionnaireResponseJson;
	
	}
	
	$scope.downloadQuestionnaireResponseJSON =  function(patient)
	{
		var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(patient.qresponse));
		var dlAnchorElem = document.getElementById('downloadAnchorElem');
		dlAnchorElem.setAttribute("href",     dataStr     );
		dlAnchorElem.setAttribute("download", patient.firstName + "_" + patient.lastName +  ".json");
		dlAnchorElem.click();
	}
	
	$scope.downloadQuestionnaireResponseCSV =  function(patient)
	{		
		var anchor = angular.element('<a/>');
		anchor.css({display: 'none'}); // Make sure it's not visible
		angular.element(document.body).append(anchor); // Attach to document

		anchor.attr({
		    href: 'data:attachment/csv;charset=utf-8,' + encodeURI(patient.questionnaireResponseCsv),
		    target: '_blank',
		    download: patient.firstName + '_' + patient.lastName + '.csv'
		})[0].click();

		anchor.remove();		
	}
	
	var processDaysSinceDischarge =  function( patient )
	{
		var dis_date = new Date( patient.dischargeDate );
		var today = new Date();
		var timeDiff = Math.abs( today.getTime() - dis_date.getTime() );
		var diffDays = parseInt(Math.ceil( timeDiff / ( 1000 * 3600 * 24 ) ),10) + 1;
		patient.days = diffDays;
	};
}]);