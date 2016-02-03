'use strict';

angular.module('inventoryappApp')
    .controller('ItemBrandDetailController', function ($scope, $rootScope, $stateParams, entity, ItemBrand) {
        $scope.itemBrand = entity;
        $scope.load = function (id) {
            ItemBrand.get({id: id}, function(result) {
                $scope.itemBrand = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:itemBrandUpdate', function(event, result) {
            $scope.itemBrand = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
