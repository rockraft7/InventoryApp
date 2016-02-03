'use strict';

angular.module('inventoryappApp').controller('UnitDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Unit',
        function($scope, $stateParams, $uibModalInstance, entity, Unit) {

        $scope.unit = entity;
        $scope.load = function(id) {
            Unit.get({id : id}, function(result) {
                $scope.unit = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:unitUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.unit.id != null) {
                Unit.update($scope.unit, onSaveSuccess, onSaveError);
            } else {
                Unit.save($scope.unit, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
