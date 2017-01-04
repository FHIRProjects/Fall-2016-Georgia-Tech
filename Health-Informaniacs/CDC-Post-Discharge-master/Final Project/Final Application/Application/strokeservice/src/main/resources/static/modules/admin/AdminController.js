'use strict';
 
angular.module('Admin')
 
.controller('AdminController',['$scope','$location','$cookies','$http','Upload','$timeout',function($scope, $location, $cookies, $http, Upload, $timeout) 
{
	$scope.init = function()
	{
		if ( !($cookies.get( 'username' ) === undefined ) )
		{
			if ($cookies.get( 'username' ) === '')
			{
				$location.path('/login');
				return;
			}
			else if ($cookies.get( 'username' ) != 'admin')
			{
				$location.path('/');
				return;
			}
		}
	};
	
	$scope.data = {};
	
	$scope.updateSchemas = function()
	{
		$http(
		{
			method: 'GET',
			url : 'cdc/api/stroke/questionnaire',
			headers : 
			{
				'username' : $cookies.get( 'username' ), 
				'password' : $cookies.get( 'password' )
			}
		})
		.success(function(data)
		{
			$scope.data = data;
			console.log(data);
		});
	}
	
	$scope.updateSchemas();
	
	$scope.uploadFile = function(file)
	{
		if (file)
		{
			file.upload = Upload.upload(
			{
				url : 'cdc/api/stroke/questionnaire',
				headers : 
				{
					'username' : $cookies.get( 'username' ), 
					'password' : $cookies.get( 'password' )
				},
				data : 
				{
					file: file
				}
			});
		}
		
		file.upload.then(function()
		{
			$timeout(function()
			{
				$scope.updateSchemas();
			});
		});
	};
	
	$scope.deleteSchema = function(id)
	{
		$http(
		{
			method: 'DELETE',
			url : 'cdc/api/stroke/questionnaire/' + id,
			headers : 
			{
				'username' : $cookies.get( 'username' ), 
				'password' : $cookies.get( 'password' )
			}
		})
		.success(function()
		{
			$scope.updateSchemas();
		});
	};
}]);