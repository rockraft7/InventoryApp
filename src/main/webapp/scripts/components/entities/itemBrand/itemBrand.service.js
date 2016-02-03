'use strict';

angular.module('inventoryappApp')
    .factory('ItemBrand', function ($resource, DateUtils) {
        return $resource('api/itemBrands/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
