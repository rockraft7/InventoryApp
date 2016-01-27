'use strict';

angular.module('inventoryappApp').controller('BookDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Book', 'Author',
        function($scope, $stateParams, $uibModalInstance, entity, Book, Author) {

        $scope.book = entity;
        $scope.authors = Author.query();
        $scope.load = function(id) {
            Book.get({id : id}, function(result) {
                $scope.book = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('inventoryappApp:bookUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.book.id != null) {
                Book.update($scope.book, onSaveSuccess, onSaveError);
            } else {
                Book.save($scope.book, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForPublicationDate = {};

        $scope.datePickerForPublicationDate.status = {
            opened: false
        };

        $scope.datePickerForPublicationDateOpen = function($event) {
            $scope.datePickerForPublicationDate.status.opened = true;
        };
}]);
