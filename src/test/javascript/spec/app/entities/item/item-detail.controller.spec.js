'use strict';

describe('Controller Tests', function() {

    describe('Item Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItem, MockItemGroup, MockItemModel, MockItemStatus, MockItemHistory, MockStockAcquisition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItem = jasmine.createSpy('MockItem');
            MockItemGroup = jasmine.createSpy('MockItemGroup');
            MockItemModel = jasmine.createSpy('MockItemModel');
            MockItemStatus = jasmine.createSpy('MockItemStatus');
            MockItemHistory = jasmine.createSpy('MockItemHistory');
            MockStockAcquisition = jasmine.createSpy('MockStockAcquisition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Item': MockItem,
                'ItemGroup': MockItemGroup,
                'ItemModel': MockItemModel,
                'ItemStatus': MockItemStatus,
                'ItemHistory': MockItemHistory,
                'StockAcquisition': MockStockAcquisition
            };
            createController = function() {
                $injector.get('$controller')("ItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventoryappApp:itemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
