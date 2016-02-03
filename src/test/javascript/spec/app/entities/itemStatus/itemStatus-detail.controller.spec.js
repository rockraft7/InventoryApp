'use strict';

describe('Controller Tests', function() {

    describe('ItemStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItemStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItemStatus = jasmine.createSpy('MockItemStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ItemStatus': MockItemStatus
            };
            createController = function() {
                $injector.get('$controller')("ItemStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventoryappApp:itemStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
