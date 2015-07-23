'use strict';

angular.module('mean.${class.label}').controller('${class.name + "Controller"}', ['$scope', '$stateParams', '$location', 'Global', '${class.name}', 'MeanUser', 'Circles',function($scope, $stateParams, $location, Global, ${class.name}, MeanUser, Circles) {
    $scope.global = Global;

    $scope.hasAuthorization = function(article) {
      if (!article || !article.user) return false;
      return MeanUser.isAdmin || article.user._id === MeanUser.user._id;
    };

    $scope.availableCircles = [];

    Circles.mine(function(acl) {
        $scope.availableCircles = acl.allowed;
        $scope.allDescendants = acl.descendants;
    });

    $scope.showDescendants = function(permission) {
        var temp = $('.ui-select-container .btn-primary').text().split(' ');
        temp.shift(); //remove close icon
        var selected = temp.join(' ');
        $scope.descendants = $scope.allDescendants[selected];
    };

    $scope.selectPermission = function() {
        $scope.descendants = [];
    };

    $scope.create = function(isValid) {
      if (isValid) {
        // $scope.article.permissions.push('test test');
        var ${class.label} = new ${class.name}($scope.${class.label});

        ${class.label}.$save(function(response) {
          $location.path('${class.label}s/' + response._id);
        });

        $scope.${class.label} = {};

      } else {
        $scope.submitted = true;
      }
    };

    $scope.remove = function(${class.label}) {
      if (${class.label}) {
        ${class.label}.$remove(function(response) {
          for (var i in $scope.${class.label}s) {
            if ($scope.${class.label}s[i] === article) {
              $scope.${class.label}s.splice(i, 1);
            }
          }
          $location.path('${class.label}s');
        });
      } else {
        $scope.${class.label}.$remove(function(response) {
          $location.path('${class.label}s');
        });
      }
    };

    $scope.update = function(isValid) {
      if (isValid) {
        var ${class.label} = $scope.${class.label};
        if (!${class.label}.updated) {
          ${class.label}.updated = [];
        }
        ${class.label}.updated.push(new Date().getTime());

        ${class.label}.$update(function() {
          $location.path('${class.label}s/' + ${class.label}._id);
        });
      } else {
        $scope.submitted = true;
      }
    };

    $scope.find = function() {
      ${class.name}.query(function(${class.label}s) {
        $scope.${class.label}s = ${class.label}s;
      });
    };

    $scope.findOne = function() {
      ${class.name}.get({
        ${class.label}Id: $stateParams.${class.label}Id
      }, function(${class.label}) {
        $scope.${class.label} = ${class.label};
      });
    };
  }
]);
