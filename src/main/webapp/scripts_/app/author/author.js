'use strict';

angular.module('inventoryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('author', {
                parent: 'site',
                url: '/authors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.author.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts_/app/author/authors.html',
                        controller: 'AuthorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('author');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    })
;
