'use strict';

angular.module('inventoryApp')
    .controller('NavbarController', function($scope, $location, $state, Principal, Auth) {
        //inject Auth, Principal here
        $scope.$state = $state;
        $scope.isAuthenticated = Principal.isAuthenticated;

        $scope.logout = function() {
            Auth.logout();
            $state.go('login');
        }
    })
;
