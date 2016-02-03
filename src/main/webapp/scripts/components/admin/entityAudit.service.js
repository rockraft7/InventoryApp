'use strict';

angular.module('inventoryappApp')
    .factory('EntityAuditService', function ($http) {
        return {
            findAllAudited: function () {
                return $http.get('api/audits/entity/all').then(function (response) {
                    return response.data;
                });
            },
            findByEntity: function (entityType, limit) {
                return $http.get('api/audits/entity/changes', {
                    params: {
                        entityType: entityType,
                        limit: limit
                    }
                }).then(function (response) {
                    return response.data;
                });
            },
            getPrevVersion: function (qualifiedName, entityId, commitVersion) {
                return $http.get('api/audits/entity/changes/version/previous', {
                    params: {
                        qualifiedName: qualifiedName,
                        entityId: entityId,
                        commitVersion: commitVersion
                    }
                }).then(function (response) {
                    return response.data;
                });
            }
        };
    });
