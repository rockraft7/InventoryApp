'use strict';

angular.module('inventoryappApp').controller('ItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Item', 'ItemGroup', 'ItemModel', 'ItemStatus', 'ItemHistory',
        function($scope, $stateParams, $uibModalInstance, entity, Item, ItemGroup, ItemModel, ItemStatus, ItemHistory) {

        $scope.item = entity;
        $scope.itemgroups = ItemGroup.query();
        $scope.itemmodels = ItemModel.query();
        $scope.itemstatuss = ItemStatus.query();
        $scope.itemhistorys = ItemHistory.query();
        $scope.load = function(id) {
            Item.get({id : id}, function(result) {
                $scope.item = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:itemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.item.id != null) {
                Item.update($scope.item, onSaveSuccess, onSaveError);
            } else {
                Item.save($scope.item, onSaveSuccess, onSaveError);
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
