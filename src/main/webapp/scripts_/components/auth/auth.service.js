'use strict';

angular.module('inventoryApp')
    .factory('Auth', function Auth($rootScope, Principal, $translate, $state, $q, AuthServerProvider, localStorageService) {
        return {
            login: function(credentials, callback) {
                var cb = callback || angular.noop;
                var deferred = $q.defer();

                AuthServerProvider.login(credentials).then(function (data) {
                    Principal.identity(true).then(function(account) {
                        console.log("account=" + angular.toJson(account));
                        $translate.use(account.langKey).then(function() {
                            $translate.refresh();
                        });
                        //TODO Tracker.sendActivity();
                        deferred.resolve(data);
                    });
                    return cb();
                }).catch(function (err) {
                    //TODO this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

                return deferred.promise;
            },
            authorize: function(force) {
                return Principal.identity(force)
                    .then(function () {
                        var isAuthenticated = Principal.isAuthenticated();

                        //TODO restrict access to login and register if isAuthenticated

                        //TODO check authentication here
                        if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0 && !Principal.hasAnyAuthority($rootScope.toState.data.authorities)) {
                            if (isAuthenticated) {
                                $state.go('accessdenied');
                            } else {
                                $rootScope.previousStateName = $rootScope.toState;
                                $rootScope.previousStateNameParams = $rootScope.toStateParams;
                                $state.go('login');
                            }
                        }
                    });
            },
            logout: function() {
                AuthServerProvider.logout();
                Principal.authenticate(null);
            }
        }
    })
;
