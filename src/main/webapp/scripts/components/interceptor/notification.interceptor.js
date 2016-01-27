 'use strict';

angular.module('inventoryappApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-inventoryappApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-inventoryappApp-params')});
                }
                return response;
            }
        };
    });
