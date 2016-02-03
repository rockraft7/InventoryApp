'use strict';

angular.module('inventoryappApp')
    .controller('ItemModelDetailController', function ($scope, $rootScope, $stateParams, entity, ItemModel, ItemBrand) {
        $scope.itemModel = entity;
        $scope.load = function (id) {
            ItemModel.get({id: id}, function(result) {
                $scope.itemModel = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:itemModelUpdate', function(event, result) {
            $scope.itemModel = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
