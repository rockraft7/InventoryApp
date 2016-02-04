'use strict';

describe('Controller Tests', function() {

    describe('StockAcquisition Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStockAcquisition, MockItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStockAcquisition = jasmine.createSpy('MockStockAcquisition');
            MockItem = jasmine.createSpy('MockItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StockAcquisition': MockStockAcquisition,
                'Item': MockItem
            };
            createController = function() {
                $injector.get('$controller')("StockAcquisitionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventoryappApp:stockAcquisitionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
