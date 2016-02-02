'use strict';

angular.module('inventoryApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate',
        'ngResource', 'ngCookies', 'ngAria', 'ngCacheBuster', 'ngFileUpload',
        'ui.bootstrap', 'ui.router', 'infinite-scroll', 'angular-loading-bar','datatables'])
    .run(function ($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, ENV, VERSION) {
        var updateTitle = function (titleKey) {
            if (!titleKey && $state.$current.data && $state.$current.data.pageTitle) {
                titleKey = $state.$current.data.pageTitle;
            }
            $translate(titleKey || 'global.title').then(function (title) {
                $window.document.title = title;
            });
        };

        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;

        $rootScope.$on("$stateChangeStart", function(event, toState, toStateParams) {
            console.log("toState: " + angular.toJson(toState));
            console.log("toStateParams: " + angular.toJson(toStateParams));
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

            Language.getCurrent().then(function(language) {
                $translate.use(language);
            });
        });

        $rootScope.$on("$stateChangeSuccess", function(event, toState, toParams, fromState, fromParams) {
            var titleKey = 'global.title';

            if (toState.name != 'login' && $rootScope.previousStateName) {
                $rootScope.previousStateName = fromState.name;
                $rootScope.previousStateParams = fromParams;
            }

            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }
            updateTitle(titleKey);
        });

        $rootScope.$on("$translateChangeSuccess", function() {
            updateTitle();
        });

        $rootScope.back = function() {
            try {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            } catch(err) {
                $state.go('home');
            }
        };
    })

    .config(function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);


        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract':true,
            views: {
                'navbar@': {
                    templateUrl:'scripts_/components/navbar/navbar.html',
                    controller: 'NavbarController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function(Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                    $translatePartialLoader.addPart('global');
                }]
            }
        });

        //TODO add interceptors here
        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');

        //initialize language translator
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });
        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();
        $translateProvider.useSanitizeValueStrategy('escaped');
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage();
        tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');
    })
;
