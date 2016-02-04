'use strict';

angular.module('inventoryappApp')
    .controller('ItemDetailController', function ($scope, $rootScope, $stateParams, entity, Item, ItemGroup, ItemModel, ItemStatus, ItemHistory, StockAcquisition) {
        $scope.item = entity;
        $scope.load = function (id) {
            Item.get({id: id}, function(result) {
                $scope.item = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:itemUpdate', function(event, result) {
            $scope.item = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
