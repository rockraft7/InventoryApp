'use strict';

angular.module('inventoryappApp').controller('ItemStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ItemStatus',
        function($scope, $stateParams, $uibModalInstance, entity, ItemStatus) {

        $scope.itemStatus = entity;
        $scope.load = function(id) {
            ItemStatus.get({id : id}, function(result) {
                $scope.itemStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:itemStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.itemStatus.id != null) {
                ItemStatus.update($scope.itemStatus, onSaveSuccess, onSaveError);
            } else {
                ItemStatus.save($scope.itemStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
