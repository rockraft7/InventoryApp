'use strict';

angular.module('inventoryappApp')
	.controller('ItemHistoryDeleteController', function($scope, $uibModalInstance, entity, ItemHistory) {

        $scope.itemHistory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ItemHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
