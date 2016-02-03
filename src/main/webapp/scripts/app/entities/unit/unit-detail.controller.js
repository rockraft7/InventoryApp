'use strict';

angular.module('inventoryappApp')
    .controller('UnitDetailController', function ($scope, $rootScope, $stateParams, entity, Unit) {
        $scope.unit = entity;
        $scope.load = function (id) {
            Unit.get({id: id}, function(result) {
                $scope.unit = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:unitUpdate', function(event, result) {
            $scope.unit = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
