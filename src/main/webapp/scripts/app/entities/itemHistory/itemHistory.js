'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itemHistory', {
                parent: 'entity',
                url: '/itemHistorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemHistory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemHistory/itemHistorys.html',
                        controller: 'ItemHistoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemHistory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('itemHistory.detail', {
                parent: 'entity',
                url: '/itemHistory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemHistory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemHistory/itemHistory-detail.html',
                        controller: 'ItemHistoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemHistory');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ItemHistory', function($stateParams, ItemHistory) {
                        return ItemHistory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('itemHistory.new', {
                parent: 'itemHistory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemHistory/itemHistory-dialog.html',
                        controller: 'ItemHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('itemHistory', null, { reload: true });
                    }, function() {
                        $state.go('itemHistory');
                    })
                }]
            })
            .state('itemHistory.edit', {
                parent: 'itemHistory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemHistory/itemHistory-dialog.html',
                        controller: 'ItemHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ItemHistory', function(ItemHistory) {
                                return ItemHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('itemHistory.delete', {
                parent: 'itemHistory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemHistory/itemHistory-delete-dialog.html',
                        controller: 'ItemHistoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ItemHistory', function(ItemHistory) {
                                return ItemHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
