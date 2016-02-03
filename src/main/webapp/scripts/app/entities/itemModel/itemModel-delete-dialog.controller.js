'use strict';

angular.module('inventoryappApp')
	.controller('ItemModelDeleteController', function($scope, $uibModalInstance, entity, ItemModel) {

        $scope.itemModel = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ItemModel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
