'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entityAudit', {
                parent: 'admin',
                url: '/entityAudits',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Audits'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entityAudit/entityAudits.html',
                        controller: 'EntityAuditController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('audits');
                        return $translate.refresh();
                    }]
                }
            });
    });
