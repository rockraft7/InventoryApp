'use strict';

angular.module('inventoryappApp')
    .factory('Item', function ($resource, DateUtils) {
        return $resource('api/items/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateAcquire = DateUtils.convertLocaleDateFromServer(data.dateAcquire);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateAcquire = DateUtils.convertLocaleDateToServer(data.dateAcquire);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateAcquire = DateUtils.convertLocaleDateToServer(data.dateAcquire);
                    return angular.toJson(data);
                }
            }
        });
    });
