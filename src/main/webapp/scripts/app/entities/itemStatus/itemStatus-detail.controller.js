'use strict';

angular.module('inventoryappApp')
    .controller('ItemStatusDetailController', function ($scope, $rootScope, $stateParams, entity, ItemStatus) {
        $scope.itemStatus = entity;
        $scope.load = function (id) {
            ItemStatus.get({id: id}, function(result) {
                $scope.itemStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('inventoryappApp:itemStatusUpdate', function(event, result) {
            $scope.itemStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
