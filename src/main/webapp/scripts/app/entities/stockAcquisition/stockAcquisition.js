'use strict';

angular.module('inventoryappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockAcquisition', {
                parent: 'entity',
                url: '/stockAcquisitions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.stockAcquisition.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockAcquisition/stockAcquisitions.html',
                        controller: 'StockAcquisitionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockAcquisition');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockAcquisition.detail', {
                parent: 'entity',
                url: '/stockAcquisition/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'inventoryappApp.stockAcquisition.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockAcquisition/stockAcquisition-detail.html',
                        controller: 'StockAcquisitionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockAcquisition');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockAcquisition', function($stateParams, StockAcquisition) {
                        return StockAcquisition.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockAcquisition.new', {
                parent: 'stockAcquisition',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stockAcquisition/stockAcquisition-dialog.html',
                        controller: 'StockAcquisitionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    invoiceId: null,
                                    dateAcquire: null,
                                    remarks: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockAcquisition', null, { reload: true });
                    }, function() {
                        $state.go('stockAcquisition');
                    })
                }]
            })
            .state('stockAcquisition.edit', {
                parent: 'stockAcquisition',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stockAcquisition/stockAcquisition-dialog.html',
                        controller: 'StockAcquisitionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockAcquisition', function(StockAcquisition) {
                                return StockAcquisition.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockAcquisition', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('stockAcquisition.delete', {
                parent: 'stockAcquisition',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stockAcquisition/stockAcquisition-delete-dialog.html',
                        controller: 'StockAcquisitionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['StockAcquisition', function(StockAcquisition) {
                                return StockAcquisition.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockAcquisition', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
