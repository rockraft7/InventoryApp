'use strict';

angular.module('inventoryappApp')
    .controller('ItemHistoryDetailController', function ($scope, $rootScope, $stateParams, entity, ItemHistory, Item, ItemStatus) {
        $scope.itemHistory = entity;
        $scope.load = function (id) {
            ItemHistory.get({id: id}, function(result) {
                $scope.itemHistory = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:itemHistoryUpdate', function(event, result) {
            $scope.itemHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
