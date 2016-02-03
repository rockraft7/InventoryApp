'use strict';

angular.module('inventoryappApp')
    .controller('ItemStatusController', function ($scope, $state, ItemStatus, ItemStatusSearch, ParseLinks) {

        $scope.itemStatuss = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ItemStatus.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.itemStatuss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ItemStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.itemStatuss = result;
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
            $scope.itemStatus = {
                tag: null,
                caption: null,
                id: null
            };
        };
    });
