'use strict';

angular.module('inventoryappApp').controller('ItemBrandDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ItemBrand',
        function($scope, $stateParams, $uibModalInstance, entity, ItemBrand) {

        $scope.itemBrand = entity;
        $scope.load = function(id) {
            ItemBrand.get({id : id}, function(result) {
                $scope.itemBrand = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:itemBrandUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.itemBrand.id != null) {
                ItemBrand.update($scope.itemBrand, onSaveSuccess, onSaveError);
            } else {
                ItemBrand.save($scope.itemBrand, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
