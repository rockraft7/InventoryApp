'use strict';

angular.module('inventoryappApp')
    .controller('ItemHistoryController', function ($scope, $state, ItemHistory, ItemHistorySearch, ParseLinks) {

        $scope.itemHistorys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ItemHistory.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.itemHistorys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ItemHistorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.itemHistorys = result;
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
            $scope.itemHistory = {
                id: null
            };
        };
    });
