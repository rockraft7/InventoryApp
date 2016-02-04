'use strict';

angular.module('inventoryappApp')
    .controller('StockAcquisitionDetailController', function ($scope, $rootScope, $stateParams, entity, StockAcquisition, Item) {
        $scope.stockAcquisition = entity;
        $scope.load = function (id) {
            StockAcquisition.get({id: id}, function(result) {
                $scope.stockAcquisition = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:stockAcquisitionUpdate', function(event, result) {
            $scope.stockAcquisition = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
