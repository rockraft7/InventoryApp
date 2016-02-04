'use strict';

describe('Controller Tests', function() {

    describe('ItemHistory Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItemHistory, MockItem, MockItemStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItemHistory = jasmine.createSpy('MockItemHistory');
            MockItem = jasmine.createSpy('MockItem');
            MockItemStatus = jasmine.createSpy('MockItemStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ItemHistory': MockItemHistory,
                'Item': MockItem,
                'ItemStatus': MockItemStatus
            };
            createController = function() {
                $injector.get('$controller')("ItemHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventoryappApp:itemHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
