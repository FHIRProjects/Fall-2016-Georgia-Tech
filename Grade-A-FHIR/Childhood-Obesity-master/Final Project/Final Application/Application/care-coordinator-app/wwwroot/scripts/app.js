'use strict';

/**
 * @ngdoc overview
 * @name stuffApp
 * @description
 * # stuffApp
 *
 * Main module of the application.
 */
angular
  .module('stuffApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'highcharts-ng',
    'ui.grid',
    'rzModule',
    'ui.bootstrap', 
    'angularShamSpinner'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/:providerID', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .otherwise({
        redirectTo: '/-1'
      });
  });
