'use strict';

angular.module('inventoryappApp').controller('StockAcquisitionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockAcquisition', 'Item',
        function($scope, $stateParams, $uibModalInstance, entity, StockAcquisition, Item) {

        $scope.stockAcquisition = entity;
        $scope.items = Item.query();
        $scope.load = function(id) {
            StockAcquisition.get({id : id}, function(result) {
                $scope.stockAcquisition = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:stockAcquisitionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.stockAcquisition.id != null) {
                StockAcquisition.update($scope.stockAcquisition, onSaveSuccess, onSaveError);
            } else {
                StockAcquisition.save($scope.stockAcquisition, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDateAcquire = {};

        $scope.datePickerForDateAcquire.status = {
            opened: false
        };

        $scope.datePickerForDateAcquireOpen = function($event) {
            $scope.datePickerForDateAcquire.status.opened = true;
        };
}]);
