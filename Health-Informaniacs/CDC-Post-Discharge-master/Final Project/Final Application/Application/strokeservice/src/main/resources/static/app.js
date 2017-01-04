'use strict';

// declare modules
angular.module('Login', []);
angular.module('Home', []);
angular.module('Questionnaire', []);
angular.module('Admin', []);

angular.module('strokeApp', [
	'ngRoute',
	'ngCookies',
	'ngSanitize',
	'rzModule',
	'ui.bootstrap',
	'ngFileUpload',
	'jsonFormatter',
	'720kb.datepicker',
	'Login',
	'Home',
	'Questionnaire',
	'Admin'
])
 
.config(['$routeProvider', function ($routeProvider) 
{
	$routeProvider
	.when('/', 
	{
		controller: 'HomeController',
		templateUrl: 'modules/home/home.html'
	})
	.when('/login', 
	{
		controller: 'LoginController',
		templateUrl: 'modules/login/login.html',
		hideMenus: true
	})
	.when('/questionnaire', 
	{
		controller: 'QuestionnaireController',
		templateUrl: 'modules/questionnaire/questionnaire.html'
	})
	.when('/admin', 
	{
		controller: 'AdminController',
		templateUrl: 'modules/admin/admin.html'
	})
	.otherwise(
	{ 
		redirectTo: '/login' 
	});
}])

.controller('MainController', ['$scope','$rootScope','$location','$cookies',function ($scope, $rootScope, $location, $cookies)
{
	$scope.init = function()
	{
		if (!$scope.isSignedIn())
		{
			$cookies.put( 'username', '' );
			$cookies.put( 'password', '' );
		}
	};
	
	$scope.logout = function() 
	{
		$cookies.put( 'username', '' );
		$cookies.put( 'password', '' );
		$location.path('/login');
	};
	
	$rootScope.username = $cookies.get( 'username' );
	
	$scope.isSignedIn = function()
	{
		if ( $cookies.get( 'username' ) != undefined )
		{
			if ( $cookies.get( 'username' ) != '' )
			{
				return true;
			}
		}
		return false;
	};
}]);