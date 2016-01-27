'use strict';

angular.module('inventoryappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


