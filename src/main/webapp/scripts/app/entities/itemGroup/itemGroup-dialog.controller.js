'use strict';

angular.module('inventoryappApp').controller('ItemGroupDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ItemGroup',
        function($scope, $stateParams, $uibModalInstance, entity, ItemGroup) {

        $scope.itemGroup = entity;
        $scope.load = function(id) {
            ItemGroup.get({id : id}, function(result) {
                $scope.itemGroup = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:itemGroupUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.itemGroup.id != null) {
                ItemGroup.update($scope.itemGroup, onSaveSuccess, onSaveError);
            } else {
                ItemGroup.save($scope.itemGroup, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
