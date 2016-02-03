'use strict';

angular.module('inventoryappApp').controller('ItemModelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ItemModel', 'ItemBrand',
        function($scope, $stateParams, $uibModalInstance, entity, ItemModel, ItemBrand) {

        $scope.itemModel = entity;
        $scope.itembrands = ItemBrand.query();
        $scope.load = function(id) {
            ItemModel.get({id : id}, function(result) {
                $scope.itemModel = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:itemModelUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.itemModel.id != null) {
                ItemModel.update($scope.itemModel, onSaveSuccess, onSaveError);
            } else {
                ItemModel.save($scope.itemModel, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
