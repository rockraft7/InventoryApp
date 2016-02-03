'use strict';

angular.module('inventoryappApp')
    .factory('ItemStatus', function ($resource, DateUtils) {
        return $resource('api/itemStatuss/:id', {}, {
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
