'use strict';

angular.module('inventoryApp')
    .factory('authInterceptor', function(localStorageService) {
        return {
            request: function(config) {
                config.headers = config.headers || {};
                var token = localStorageService.get('token');

                if (token) {
                    console.log(token.expires_at + ' > ' + new Date().getTime());
                }

                if (token && token.expires_at && token.expires_at > new Date().getTime()) {
                    console.log("Adding Bearer " + token.access_token);
                    config.headers.Authorization = 'Bearer ' + token.access_token;
                } else {

                }

                return config;
            }
        }
    })

    .factory('authExpiredInterceptor', function(localStorageService, $q, $injector) {
        return {
            responseError: function(response) {
                if (response.status === 401 && ((response.data.error == 'invalid_token') || response.data.error == 'Unauthorized')) {
                    console.log("Removing token.");
                    localStorageService.remove('token');
                    var Principal = $injector.get('Principal');
                    if (Principal.isAuthenticated()) {
                        var Auth = $injector.get('Auth');
                        Auth.authorize(true);
                    }
                }
                return $q.reject(response);
            }
        }
    })
;
