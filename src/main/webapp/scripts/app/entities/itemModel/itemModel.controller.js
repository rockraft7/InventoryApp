'use strict';

angular.module('inventoryappApp')
    .controller('ItemModelController', function ($scope, $state, ItemModel, ItemModelSearch, ParseLinks) {

        $scope.itemModels = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ItemModel.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.itemModels = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ItemModelSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.itemModels = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.itemModel = {
                name: null,
                description: null,
                id: null
            };
        };
    });
