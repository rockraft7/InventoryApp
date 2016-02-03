'use strict';

angular.module('inventoryappApp')
	.controller('ItemBrandDeleteController', function($scope, $uibModalInstance, entity, ItemBrand) {

        $scope.itemBrand = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ItemBrand.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
