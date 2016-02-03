'use strict';

angular.module('inventoryappApp')
    .factory('ItemGroupSearch', function ($resource) {
        return $resource('api/_search/itemGroups/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
