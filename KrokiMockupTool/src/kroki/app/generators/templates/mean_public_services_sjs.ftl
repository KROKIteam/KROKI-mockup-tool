'use strict';

//${class.name} service used for ${class.label}s REST endpoint
angular.module('mean.${class.label}s').factory('${class.name}', ['$resource',
  function($resource) {
    return $resource('api/${class.label}s/:${class.label}Id', {
      ${class.label}Id: '@_id'
    }, {
      update: {
        method: 'PUT'
      }
    });
  }
]);
