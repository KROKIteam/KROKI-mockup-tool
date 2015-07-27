'use strict';

angular.module('mean.${klasa.label}').controller('${klasa.name + "Controller"}', ['$scope', '$stateParams', '$location', 'Global', '${klasa.name}', 'MeanUser', 'Circles',function($scope, $stateParams, $location, Global, ${klasa.name}, MeanUser, Circles) {
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
        var ${klasa.label} = new ${klasa.name}($scope.${klasa.label});

        ${klasa.label}.$save(function(response) {
          $location.path('${klasa.label}s/' + response._id);
        });

        $scope.${klasa.label} = {};

      } else {
        $scope.submitted = true;
      }
    };

    $scope.remove = function(${klasa.label}) {
      if (${klasa.label}) {
        ${klasa.label}.$remove(function(response) {
          for (var i in $scope.${klasa.label}s) {
            if ($scope.${klasa.label}s[i] === article) {
              $scope.${klasa.label}s.splice(i, 1);
            }
          }
          $location.path('${klasa.label}s');
        });
      } else {
        $scope.${klasa.label}.$remove(function(response) {
          $location.path('${klasa.label}s');
        });
      }
    };

    $scope.update = function(isValid) {
      if (isValid) {
        var ${klasa.label} = $scope.${klasa.label};
        if (!${klasa.label}.updated) {
          ${klasa.label}.updated = [];
        }
        ${klasa.label}.updated.push(new Date().getTime());

        ${klasa.label}.$update(function() {
          $location.path('${klasa.label}s/' + ${klasa.label}._id);
        });
      } else {
        $scope.submitted = true;
      }
    };

    $scope.find = function() {
      ${klasa.name}.query(function(${klasa.label}s) {
        $scope.${klasa.label}s = ${klasa.label}s;
      });
    };

    $scope.findOne = function() {
      ${klasa.name}.get({
        ${klasa.label}Id: $stateParams.${klasa.label}Id
      }, function(${klasa.label}) {
        $scope.${klasa.label} = ${klasa.label};
      });
    };
  }
]);
