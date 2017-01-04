'use strict';

angular.module('Login')

.controller('LoginController', ['$rootScope', '$http','$scope','$location', '$cookies', function($rootScope, $http, $scope, $location, $cookies) 
{
	$scope.init = function()
	{
		if ( !($cookies.get( 'username' ) === undefined ) )
		{
			if ($cookies.get( 'username' ) != '')
			{
				if ( $cookies.get( 'username' ) === 'admin' )
				{
					$location.path('/admin');
					return;
				}
				else
				{
					$location.path('/');
					return;
				}
			}
		}
	};
	
	$scope.login = function() 
	{	
		$scope.dataLoading = true;
		
		$http(
		{
			method: 'POST',
			url : '/cdc/api/stroke/authenticate',
			headers : 
			{
				'username' : $scope.username, 
				'password' : $scope.password
			}
		})
		.success(function (response)
		{
			if (response.AuthenticationSuccess === 'Success') 
			{
				$cookies.put( 'username', $scope.username );
				$cookies.put( 'password', $scope.password );
				
				$rootScope.username = $cookies.get( 'username' );
				
                $location.path('/');
			}
			else if (response.AuthenticationSuccess === 'Failure') 
			{
				$scope.errorMessage = 'Username or password is incorrect. Re-enter username and password.';
				$scope.dataLoading = false;
				$location.path('/login');
			}
			else 
			{
				$scope.error = response.message;
				$scope.dataLoading = false;
			}
		});
	};
}]);