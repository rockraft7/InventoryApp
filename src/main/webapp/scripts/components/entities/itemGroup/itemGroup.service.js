'use strict';

angular.module('inventoryappApp')
    .factory('ItemGroup', function ($resource, DateUtils) {
        return $resource('api/itemGroups/:id', {}, {
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
