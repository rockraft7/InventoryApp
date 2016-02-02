'use strict';

angular.module('inventoryApp')
    .controller('LoginController', function($rootScope, $state, $scope, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};
        $scope.rememberMe = true;

        $timeout(function() {
            angular.element('[ng-model="username"]').focus();
        });
        $scope.login = function(event) {
            event.preventDefault();
            Auth.login({
                username:$scope.username,
                password:$scope.password,
                rememberMe:$scope.rememberMe
            }).then(function() {
                $scope.authenticationError = false;
                $rootScope.back();
                /*
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
                */
            }).catch(function() {
                $scope.authenticationError = true;
            });
        };
    })
;
