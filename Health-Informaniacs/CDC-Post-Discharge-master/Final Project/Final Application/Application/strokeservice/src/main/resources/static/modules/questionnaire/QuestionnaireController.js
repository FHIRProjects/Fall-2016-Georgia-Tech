//'use strict';
 
angular.module('Questionnaire')

.directive('ngBindHtmlCompile', ['$compile', function ( $compile ) {
	return {
		restrict: 'A',
		link: function ( scope, element, attrs ) {
			scope.$watch( function () {
				return scope.$eval( attrs.ngBindHtmlCompile );
			}, 
			function ( value ) {
				element.html( value && value.toString() );
                var compileScope = scope;
                if ( attrs.bindHtmlScope ) {
                    compileScope = scope.$eval( attrs.bindHtmlScope );
                }
                $compile( element.contents() )( compileScope );
			});
		}
	};
}])

//https://github.com/ilovett/angular-dom-events
.directive('domOnDestroy', function($parse) {
	return {
		link: function postLink( scope, element, attrs ) {
			var destroyHandler;
			if ( attrs.domOnDestroy ) {
				destroyHandler = $parse( attrs.domOnDestroy );
				element.on( '$destroy', function() {
					destroyHandler( scope );
				});
			}
		}
	};
})

.controller('QuestionnaireController', ['$rootScope','$scope','$location','$http','$cookies','$sce','$parse','$timeout',function($rootScope, $scope, $location, $http, $cookies, $sce, $parse, $timeout) 
{
	$scope.answers = {};
	$scope.parentValue = {};
	$scope.parentKey = {};
	
	$scope.optionsCodeAndText = {};
	
	var htmlForm = "<form name='questionnaireHtmlForm' ng-submit='submitForm()'>";
	
	$scope.init = function() 
	{
		if ( !( $cookies.get( 'username' ) === undefined ) ) 
		{
			if ( $cookies.get( 'username' ) === '' ) 
			{
				$location.path( '/login' );
				return;
			}
			else if ( $cookies.get( 'username' ) === 'admin' ) 
			{
				$location.path( '/admin' );
				return;
			}
			else if ( $rootScope.selectedPatient === undefined  || $rootScope.selectedPatient === '' ) 
			{
				$location.path('/');
				return;
			}
		}
		
		$http(
		{
			method: 'GET',
			url : '/cdc/api/stroke/questionnaire',
			headers : 
			{
				'username' : $cookies.get( 'username' ), 
				'password' : $cookies.get( 'password' )
			}
		})
		.success( function ( response ) 
		{
			var data = response[ response.length - 1 ].json;
			$scope.questionnaire = data;
			
            angular.forEach( data.group.question, function( qu ) 
            {
            		processQuestion( qu, data.group, 1 );
            });
            
            angular.forEach( data.group.group, function( gr ) 
            {
            		processGroup( gr, undefined, 1 );
            });
            
            htmlForm = htmlForm + '</form>';
            
        		$scope.questionnaireForm = $sce.trustAsHtml( htmlForm );
		});
	};
	
	// ***********************
	// PROCESS GROUP
	// ***********************
	var processGroup = function( group, parentQuestion, level ) 
	{
		var code = '';
		if ( group.extension != undefined ) 
		{
			if ( group.extension[ 0 ].extension != undefined ) 
			{
				if ( group.extension[ 0 ].extension[ 1 ].valueCoding != undefined ) 
				{
					code = group.extension[ 0 ].extension[ 1 ].valueCoding.code;
				}
			}
		}
		
		var temp = ( parentQuestion === undefined ) ? group.linkId : parentQuestion.linkId;
		var groupIdKey = "key_" + temp.split(".").join("_");
		$parse( groupIdKey ).assign( $scope, {} );
		
		var groupId = code;
		var groupTitle = ( group.title === undefined ) ? "" : " " + group.title;
		var marginLeft = ( level === 1 ) ? 'margin-bottom:40px;' : 'margin-left:40px;margin-bottom:10px;';
		
		validatedKeyValue("group", group.linkId, "", groupTitle, "0", "1");
		
		var ngShow = "";
		if ( level != 1 )
		{
			//ngShow = "' ng-if='parentValue." + groupIdKey + "===" + groupId + "'";
			ngShow = "' ng-if=" + groupIdKey + "==='" + groupId + "'"; 
		}
		
		var panelHeaderHtml = ( group.title === undefined ) ? 
				"" : "<div class='panel-heading'><h4 class='panel-title'>" + groupTitle + "</h4></div>";
		
				htmlForm = htmlForm + 
				"<div id='parentValue." + groupIdKey + "' class='panel " + getPanelTypeClass( level ) + "' " + 
					ngShow + " style='margin-bottom:0px;" + marginLeft + "'>" +
					panelHeaderHtml +
					"<div class='panel-body'>";
				
		        angular.forEach( group.question, function( qu ) 
		        {
		        	processQuestion( qu, group, (level + 1) );
		        });
		        
		        angular.forEach( group.group, function( gr ) 
		        {
		        	processGroup( gr, undefined, ( level + 1 ) );
		        });
        
        htmlForm = htmlForm + "</div></div>";
	};
	
	// ***********************
	// PROCESS QUESTION
	// ***********************
	var processQuestion = function( question, parentGroup, level ) 
	{
		var key = "key_" + question.linkId.split(".").join("_");
		$parse( key ).assign( $scope.parentKey, {} );
		$parse( key ).assign( $scope.parentValue, '' );
		
		$scope.parentKey[ key ] = key;
		$scope.answers[ question.linkId ] = undefined;
		
		var answerHtmlInputElement = undefined;
		
		if ( question.type === 'choice' || question.type === 'open-choice' || question.type === 'boolean' ) 
		{
			$scope.optionsCodeAndText[ question.linkId ] = [];
			
			if ( level === 2 ) // parent scope
			{
				answerHtmlInputElement = 
					"<select id='" + question.linkId + "'" + 
						" ng-model='" + key + "'" +
						" dom-on-destroy='elementDestroyed(parentKey." + key + ")'" +
						" class='form-control'" +
						" style='margin-bottom:20px;width:40%;'>";
			}
			else
			{
				answerHtmlInputElement = 
					"<select id='" + question.linkId + "'" + 
					 	" ng-model='parentValue." + key + "'" +
						" dom-on-destroy='elementDestroyed(parentKey." + key + ")'" +
						" class='form-control'" +
						" style='margin-bottom:20px;width:40%;'>";
			}
			
			// add an empty entry to options and assign its key/value to the scope so that it gets selected
			$parse( '_empty_' + key ).assign( $scope, '' );
			answerHtmlInputElement = answerHtmlInputElement + "<option id='_empty_" + key + "' value=''></option>";
			
			angular.forEach( question.option, function( op ) 
			{
				var value = op.code;
				answerHtmlInputElement = answerHtmlInputElement + 
        			"<option id='"+ value + "' value='" + value + "'>" + op.display + "</option>";
	
				var info = 
				{
					"code": value,
					"display": op.display
				}
				$scope.optionsCodeAndText[ question.linkId ].push( info );
			});

			
        	answerHtmlInputElement = answerHtmlInputElement + "</select>";
		}
		else if ( question.type === 'integer' || question.type === 'decimal' ) 
		{
			var minValue;
			var maxValue;
			
			if ( question.extension != undefined ) 
			{
				if ( question.extension[ 0 ].url != undefined ) 
				{
					if ( question.extension[ 0 ].url === 'http://hl7.org/fhir/StructureDefinition/minValue' ) 
					{
						minValue = question.extension[ 0 ].valueDecimal;
					}
					else if ( question.extension[ 0 ].url === 'http://hl7.org/fhir/StructureDefinition/maxValue' ) 
					{
						maxValue = question.extension[ 0 ].valueDecimal;
					}
				}
				
				if ( question.extension[ 1 ].url != undefined ) 
				{
					if ( question.extension[ 1 ].url === 'http://hl7.org/fhir/StructureDefinition/minValue' ) 
					{
						minValue = question.extension[ 1 ].valueDecimal;
					}
					else if ( question.extension[ 1 ].url === 'http://hl7.org/fhir/StructureDefinition/maxValue' ) 
					{
						maxValue = question.extension[ 1 ].valueDecimal;
					}
				}
			}
			
			// https://github.com/angular-slider/angularjs-slider
			var options = 
			{ 
				"value": minValue, 
				"options": 
				{ 
					"floor": minValue, 
					"ceil": maxValue 
				}
			};
			$parse( key + "_op" ).assign( $scope, options );
			answerHtmlInputElement = 
				"<rzslider id='" + key + "'" +
					" rz-slider-model='" + key + "_op.value'" + 
					" rz-slider-options='" + key + "_op.options'" +
					" dom-on-destroy='elementDestroyed(parentKey." + key + ")'" + 
					" style='width:50%;'></rzslider>";
			$scope.$on( "slideEnded", function() 
			{
				// user finished sliding a handle
				validatedKeyValue( question.type, question.linkId, parentGroup.linkId, question.text, $scope[ key + '_op' ].value, undefined );
			});
		}
		else if ( question.type === 'dateTime' ) 
		{
			if ( question.linkId === '1.1' ) 
			{
				answerHtmlInputElement = 
					"<input disabled type='text' id='" + key + 
						"'class='form-control' style='width:40%;' value='" + 
						$rootScope.selectedPatient.dischargeDate + "'>";
				
				validatedKeyValue( question.type, question.linkId, parentGroup.linkId, question.text, $rootScope.selectedPatient.dischargeDate, undefined );
			}
			else if ( question.linkId === '2.1' ) 
			{
				answerHtmlInputElement = 
					"<input disabled type='text' id='" + key + 
						"'class='form-control' style='width:40%;' value='" + 
						$rootScope.selectedPatient.admitDate + "'>";
				
				validatedKeyValue( question.type, question.linkId, parentGroup.linkId, question.text, $rootScope.selectedPatient.admitDate, undefined );
			}
			else
			{
				// https://github.com/720kb/angular-datepicker
				answerHtmlInputElement = 
					"<datepicker id=date_'" + key + "' date-format='yyyy-MM-dd' style='width:40%;margin-bottom:10px'>" +
						"<input dom-on-destroy='elementDestroyed(parentKey." + key + ")'" + 
						" ng-model='parentValue." + key + "' type='text' style='width:100%;background-color:white' class='form-control' readonly>";
					"</datepicker>";
			}
		}
		else
		{
			answerHtmlInputElement = 
				"<input type='text' id='"+ key + 
					" dom-on-destroy='elementDestroyed(parentKey." + key + ")'" +
					"' class='form-control' style='width:40%;' ng-model='parentValue." + key + "'>";
		}
		
		$scope.$watch( 'parentValue.' + key, function( newVal, oldVal ) 
		{
			validatedKeyValue( question.type, question.linkId, parentGroup.linkId, question.text, newVal, oldVal );
		});
		
		$scope.$watch( key, function( newVal, oldVal ) 
		{
			validatedKeyValue( question.type, question.linkId, parentGroup.linkId, question.text, newVal, oldVal );
		});
		
		htmlForm = htmlForm + "<div class='row' style='margin-left:10px'><h5>" + question.text + "</h5>" + answerHtmlInputElement + "</div>";
		
        angular.forEach( question.group, function( gr ) 
        {
        		processGroup( gr, question, ( level + 1 ) );
        });
	};
	
	$scope.elementDestroyed = function( id ) 
	{
		id = id.replace("key_", "");
		id = id.split("_").join(".");
		$scope.answers[ id ] = undefined;
	}
	
	var getPanelTypeClass = function( level ) 
	{
		if ( level === 1 ) return 'panel-info';
		else if ( level === 2 ) return 'panel-primary';
		else if ( level === 3 ) return 'panel-success';
		else if ( level === 4 ) return 'panel-danger';
		else if ( level === 5 ) return 'panel-warning';
		
		return 'panel-info';
	};
	
	var validatedKeyValue = function( field, id, parentId, text, newVal, oldVal )
	{
		if ( newVal != undefined && newVal != oldVal )
		{
			if ( field === 'choice' || field === 'open-choice' || field === 'boolean' )
			{
				var o = $scope.optionsCodeAndText[ id ];
				
				for ( var property in o ) 
				{
					if ( o[ property ].code === newVal )
					{
						newVal = o[ property ].display;
					}
				}
			}
			
			var obj = 
			{
				"id": id,
				"field": field,
				"text": text,
				"parentId": parentId,
				"type": getAnswerValueType( field ),
				"value": newVal
			};
			
			$scope.answers[ id ] = obj;
		}
	};
	
	var getAnswerValueType = function( field )
	{
		var type = "valueString";
		if ( field === 'integer' ) 
		{
			type = "valueInteger";
		}
		else if ( field === 'decimal' )
		{
			type = 'valueDecimal';
		}
		else if ( field === 'dateTime' ) 
		{
			type = 'valueDateTime';
		}
		
		return type;
	};
	
	$scope.submitForm = function() 
	{
		for ( var property in $scope.answers ) 
		{
			if ( $scope.answers[ property ] === undefined )
			{
				delete $scope.answers[ property ];
			}
		}

		$http(
		{
			method: 'POST',
			url : '/cdc/api/stroke/questionnaire-response?patientid=' + $rootScope.selectedPatient.patientId.mrn +
			 '&encounterid=' + $rootScope.selectedPatient.patientId.encounterId + 
			 '&destinationid=' + $rootScope.selectedPatient.patientId.destinationId,
			headers : 
			{
				'username' : $cookies.get( 'username' ), 
				'password' : $cookies.get( 'password' )
			},
			data : $scope.answers			
		})
		.success(function (response)
		{
			$location.path('/');
		});
	};
	
	$scope.cancelForm = function() 
	{
		$location.path('/');
	};
}]);
