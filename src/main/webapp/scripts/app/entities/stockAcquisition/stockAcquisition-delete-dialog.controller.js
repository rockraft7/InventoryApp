'use strict';

angular.module('inventoryappApp')
	.controller('StockAcquisitionDeleteController', function($scope, $uibModalInstance, entity, StockAcquisition) {

        $scope.stockAcquisition = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            StockAcquisition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
