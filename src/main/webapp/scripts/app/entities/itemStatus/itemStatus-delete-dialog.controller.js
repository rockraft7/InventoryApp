'use strict';

angular.module('inventoryappApp')
	.controller('ItemStatusDeleteController', function($scope, $uibModalInstance, entity, ItemStatus) {

        $scope.itemStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ItemStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
