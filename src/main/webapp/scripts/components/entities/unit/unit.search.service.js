'use strict';

angular.module('inventoryappApp')
    .factory('UnitSearch', function ($resource) {
        return $resource('api/_search/units/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
