'use strict';

angular.module('inventoryappApp')
    .controller('ItemGroupController', function ($scope, $state, ItemGroup, ItemGroupSearch, ParseLinks) {

        $scope.itemGroups = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ItemGroup.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.itemGroups = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ItemGroupSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.itemGroups = result;
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
            $scope.itemGroup = {
                name: null,
                tag: null,
                description: null,
                id: null
            };
        };
    });
