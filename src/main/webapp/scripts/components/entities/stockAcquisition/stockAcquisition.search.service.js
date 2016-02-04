'use strict';

angular.module('inventoryappApp')
    .factory('StockAcquisitionSearch', function ($resource) {
        return $resource('api/_search/stockAcquisitions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
