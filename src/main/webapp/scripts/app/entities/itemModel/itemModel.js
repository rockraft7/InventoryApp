'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itemModel', {
                parent: 'entity',
                url: '/itemModels',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemModel.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemModel/itemModels.html',
                        controller: 'ItemModelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemModel');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('itemModel.detail', {
                parent: 'entity',
                url: '/itemModel/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemModel.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemModel/itemModel-detail.html',
                        controller: 'ItemModelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemModel');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ItemModel', function($stateParams, ItemModel) {
                        return ItemModel.get({id : $stateParams.id});
                    }]
                }
            })
            .state('itemModel.new', {
                parent: 'itemModel',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemModel/itemModel-dialog.html',
                        controller: 'ItemModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('itemModel', null, { reload: true });
                    }, function() {
                        $state.go('itemModel');
                    })
                }]
            })
            .state('itemModel.edit', {
                parent: 'itemModel',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemModel/itemModel-dialog.html',
                        controller: 'ItemModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ItemModel', function(ItemModel) {
                                return ItemModel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemModel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('itemModel.delete', {
                parent: 'itemModel',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemModel/itemModel-delete-dialog.html',
                        controller: 'ItemModelDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ItemModel', function(ItemModel) {
                                return ItemModel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemModel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
