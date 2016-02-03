'use strict';

angular.module('inventoryappApp')
	.controller('ItemGroupDeleteController', function($scope, $uibModalInstance, entity, ItemGroup) {

        $scope.itemGroup = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ItemGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
