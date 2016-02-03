'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('unit', {
                parent: 'entity',
                url: '/units',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.unit.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/unit/units.html',
                        controller: 'UnitController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('unit');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('unit.detail', {
                parent: 'entity',
                url: '/unit/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.unit.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/unit/unit-detail.html',
                        controller: 'UnitDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('unit');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Unit', function($stateParams, Unit) {
                        return Unit.get({id : $stateParams.id});
                    }]
                }
            })
            .state('unit.new', {
                parent: 'unit',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/unit/unit-dialog.html',
                        controller: 'UnitDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    type: null,
                                    symbol: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('unit', null, { reload: true });
                    }, function() {
                        $state.go('unit');
                    })
                }]
            })
            .state('unit.edit', {
                parent: 'unit',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/unit/unit-dialog.html',
                        controller: 'UnitDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Unit', function(Unit) {
                                return Unit.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('unit', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('unit.delete', {
                parent: 'unit',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/unit/unit-delete-dialog.html',
                        controller: 'UnitDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Unit', function(Unit) {
                                return Unit.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('unit', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
