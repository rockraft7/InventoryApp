'use strict';

angular.module('inventoryappApp')
    .factory('ItemStatusSearch', function ($resource) {
        return $resource('api/_search/itemStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
