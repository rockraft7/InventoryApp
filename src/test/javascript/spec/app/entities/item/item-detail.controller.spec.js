'use strict';

describe('Controller Tests', function() {

    describe('Item Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItem, MockItemGroup, MockItemModel, MockUnit, MockItemStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItem = jasmine.createSpy('MockItem');
            MockItemGroup = jasmine.createSpy('MockItemGroup');
            MockItemModel = jasmine.createSpy('MockItemModel');
            MockUnit = jasmine.createSpy('MockUnit');
            MockItemStatus = jasmine.createSpy('MockItemStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Item': MockItem,
                'ItemGroup': MockItemGroup,
                'ItemModel': MockItemModel,
                'Unit': MockUnit,
                'ItemStatus': MockItemStatus
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
