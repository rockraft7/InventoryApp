'use strict';

angular.module('inventoryappApp')
    .factory('ItemModel', function ($resource, DateUtils) {
        return $resource('api/itemModels/:id', {}, {
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
