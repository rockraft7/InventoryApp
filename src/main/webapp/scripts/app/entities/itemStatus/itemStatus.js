'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itemStatus', {
                parent: 'entity',
                url: '/itemStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemStatus.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemStatus/itemStatuss.html',
                        controller: 'ItemStatusController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('itemStatus.detail', {
                parent: 'entity',
                url: '/itemStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemStatus.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemStatus/itemStatus-detail.html',
                        controller: 'ItemStatusDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ItemStatus', function($stateParams, ItemStatus) {
                        return ItemStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('itemStatus.new', {
                parent: 'itemStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemStatus/itemStatus-dialog.html',
                        controller: 'ItemStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    tag: null,
                                    caption: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('itemStatus', null, { reload: true });
                    }, function() {
                        $state.go('itemStatus');
                    })
                }]
            })
            .state('itemStatus.edit', {
                parent: 'itemStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemStatus/itemStatus-dialog.html',
                        controller: 'ItemStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ItemStatus', function(ItemStatus) {
                                return ItemStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('itemStatus.delete', {
                parent: 'itemStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemStatus/itemStatus-delete-dialog.html',
                        controller: 'ItemStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ItemStatus', function(ItemStatus) {
                                return ItemStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
