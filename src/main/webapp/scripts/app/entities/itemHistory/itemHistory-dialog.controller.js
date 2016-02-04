'use strict';

angular.module('inventoryappApp').controller('ItemHistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ItemHistory', 'Item', 'ItemStatus',
        function($scope, $stateParams, $uibModalInstance, entity, ItemHistory, Item, ItemStatus) {

        $scope.itemHistory = entity;
        $scope.items = Item.query();
        $scope.itemstatuss = ItemStatus.query();
        $scope.load = function(id) {
            ItemHistory.get({id : id}, function(result) {
                $scope.itemHistory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:itemHistoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.itemHistory.id != null) {
                ItemHistory.update($scope.itemHistory, onSaveSuccess, onSaveError);
            } else {
                ItemHistory.save($scope.itemHistory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
