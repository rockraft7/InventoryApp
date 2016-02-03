'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itemGroup', {
                parent: 'entity',
                url: '/itemGroups',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemGroup.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemGroup/itemGroups.html',
                        controller: 'ItemGroupController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemGroup');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('itemGroup.detail', {
                parent: 'entity',
                url: '/itemGroup/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemGroup.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemGroup/itemGroup-detail.html',
                        controller: 'ItemGroupDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemGroup');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ItemGroup', function($stateParams, ItemGroup) {
                        return ItemGroup.get({id : $stateParams.id});
                    }]
                }
            })
            .state('itemGroup.new', {
                parent: 'itemGroup',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemGroup/itemGroup-dialog.html',
                        controller: 'ItemGroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    tag: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('itemGroup', null, { reload: true });
                    }, function() {
                        $state.go('itemGroup');
                    })
                }]
            })
            .state('itemGroup.edit', {
                parent: 'itemGroup',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemGroup/itemGroup-dialog.html',
                        controller: 'ItemGroupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ItemGroup', function(ItemGroup) {
                                return ItemGroup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemGroup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('itemGroup.delete', {
                parent: 'itemGroup',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemGroup/itemGroup-delete-dialog.html',
                        controller: 'ItemGroupDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ItemGroup', function(ItemGroup) {
                                return ItemGroup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemGroup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
