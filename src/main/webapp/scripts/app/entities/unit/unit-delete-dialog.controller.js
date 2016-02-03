'use strict';

angular.module('inventoryappApp')
	.controller('UnitDeleteController', function($scope, $uibModalInstance, entity, Unit) {

        $scope.unit = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Unit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
