'use strict';

angular.module('inventoryApp')
    .controller('AuthorController', function($scope, Author, ParseLinks) {
        $scope.authors = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;

        $scope.loadAll = function() {
            Author.query({
                page: $scope.page - 1,
                size: 5,
                sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc')]
            }, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.authors = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

    })
;
