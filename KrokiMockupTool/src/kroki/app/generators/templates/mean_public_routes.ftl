'use strict';

//Setting up route
angular.module('mean.${class.label}').config(['$stateProvider',
  function($stateProvider) {

    // states for my app
    $stateProvider
      .state('all ${class.label}s', {
        url: '/${class.label}s',
        templateUrl: '/${class.label}s/views/list.html',
        resolve: {
          loggedin: function(MeanUser) {
            return MeanUser.checkLoggedin();
          }
        }
      })
      .state('create ${class.label}', {
        url: '/${class.label}s/create',
        templateUrl: '/${class.label}s/views/create.html',
        resolve: {
          loggedin: function(MeanUser) {
            return MeanUser.checkLoggedin();
          }
        }
      })
      .state('edit ${class.label}', {
        url: '/${class.label}s/:${class.label}Id/edit',
        templateUrl: '/${class.label}s/views/edit.html',
        resolve: {
          loggedin: function(MeanUser) {
            return MeanUser.checkLoggedin();
          }
        }
      })
      .state('${class.label} by id', {
        url: '/${class.label}s/:${class.label}Id',
        templateUrl: '/${class.label}s/views/view.html',
        resolve: {
          loggedin: function(MeanUser) {
            return MeanUser.checkLoggedin();
          }
        }
      });
  }
]);
