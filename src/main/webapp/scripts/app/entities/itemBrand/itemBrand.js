'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itemBrand', {
                parent: 'entity',
                url: '/itemBrands',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemBrand.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemBrand/itemBrands.html',
                        controller: 'ItemBrandController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemBrand');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('itemBrand.detail', {
                parent: 'entity',
                url: '/itemBrand/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.itemBrand.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemBrand/itemBrand-detail.html',
                        controller: 'ItemBrandDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemBrand');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ItemBrand', function($stateParams, ItemBrand) {
                        return ItemBrand.get({id : $stateParams.id});
                    }]
                }
            })
            .state('itemBrand.new', {
                parent: 'itemBrand',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemBrand/itemBrand-dialog.html',
                        controller: 'ItemBrandDialogController',
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
                        $state.go('itemBrand', null, { reload: true });
                    }, function() {
                        $state.go('itemBrand');
                    })
                }]
            })
            .state('itemBrand.edit', {
                parent: 'itemBrand',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemBrand/itemBrand-dialog.html',
                        controller: 'ItemBrandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ItemBrand', function(ItemBrand) {
                                return ItemBrand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemBrand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('itemBrand.delete', {
                parent: 'itemBrand',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itemBrand/itemBrand-delete-dialog.html',
                        controller: 'ItemBrandDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ItemBrand', function(ItemBrand) {
                                return ItemBrand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemBrand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
