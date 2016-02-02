'use strict';

angular.module('inventoryApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Hello'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts_/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {
                    //TODO do translation here
                }
            });
    })
;
