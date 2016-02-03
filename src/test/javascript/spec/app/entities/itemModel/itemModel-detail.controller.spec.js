'use strict';

describe('Controller Tests', function() {

    describe('ItemModel Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItemModel, MockItemBrand;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItemModel = jasmine.createSpy('MockItemModel');
            MockItemBrand = jasmine.createSpy('MockItemBrand');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ItemModel': MockItemModel,
                'ItemBrand': MockItemBrand
            };
            createController = function() {
                $injector.get('$controller')("ItemModelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventoryappApp:itemModelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
