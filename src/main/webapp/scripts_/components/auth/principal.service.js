'use strict';

angular.module('inventoryApp')
    .factory('Principal', function Principal($q, Account) {
        var _identity, _authenticated = false;

        return {
            isIdentityResolved: function() {
                return angular.isDefined(_identity);
            },
            isAuthenticated: function() {
                return _authenticated;
            },
            hasAnyAuthority: function(authorities) {
                if (!_authenticated || !_identity || !_identity.authorities) {
                    return false;
                }

                for(var i=0; i<authorities.length; i++) {
                    if (_identity.authorities.indexOf(authorities[i]) != -1) {
                        return true;
                    }
                }
            },
            identity: function(force) {
                var deferred = $q.defer();

                if (force === true) {
                    _identity = undefined;
                }

                if (angular.isDefined(_identity)) {
                    deferred.resolve(_identity);

                    return deferred.promise;
                }

                Account.get().$promise
                    .then(function (account) {
                        _identity = account.data;
                        _authenticated = true;
                        deferred.resolve(_identity);
                        //TODO Tracker.connect()
                    })
                    .catch(function() {
                        _identity = null;
                        _authenticated = false;
                        deferred.resolve(_identity);
                    });
                return deferred.promise;
            },
            authenticate: function(identity) {
                _identity = identity;
                _authenticated = identity !== null;
            }
        }
    })
;
