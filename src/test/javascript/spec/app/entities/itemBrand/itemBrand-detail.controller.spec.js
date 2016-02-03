'use strict';

describe('Controller Tests', function() {

    describe('ItemBrand Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItemBrand;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItemBrand = jasmine.createSpy('MockItemBrand');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ItemBrand': MockItemBrand
            };
            createController = function() {
                $injector.get('$controller')("ItemBrandDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inventoryappApp:itemBrandUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
