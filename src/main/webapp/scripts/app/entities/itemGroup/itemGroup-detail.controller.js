'use strict';

angular.module('inventoryappApp')
    .controller('ItemGroupDetailController', function ($scope, $rootScope, $stateParams, entity, ItemGroup) {
        $scope.itemGroup = entity;
        $scope.load = function (id) {
            ItemGroup.get({id: id}, function(result) {
                $scope.itemGroup = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:itemGroupUpdate', function(event, result) {
            $scope.itemGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
