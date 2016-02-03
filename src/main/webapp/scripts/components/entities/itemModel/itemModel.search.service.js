'use strict';

angular.module('inventoryappApp')
    .factory('ItemModelSearch', function ($resource) {
        return $resource('api/_search/itemModels/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
