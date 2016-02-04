'use strict';

angular.module('inventoryappApp')
    .factory('ItemHistorySearch', function ($resource) {
        return $resource('api/_search/itemHistorys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
