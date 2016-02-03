'use strict';

angular.module('inventoryappApp')
    .factory('ItemBrandSearch', function ($resource) {
        return $resource('api/_search/itemBrands/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
