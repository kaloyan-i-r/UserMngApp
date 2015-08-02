angular.module('userDemoApp', ['ngRoute', 'ngCookies', 'ui.bootstrap','ui-notification'])
    .config(function ($routeProvider, $httpProvider) {

        $routeProvider.when('/home', {
            templateUrl: '/partials/home.html',
            controller: 'userListCtrl'
        }).when('/login', {
            templateUrl: '/partials/login.html',
            controller: 'LoginCtrl'
        }).otherwise({redirectTo: '/home'});

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

        var interceptor = ['$location', '$q', function ($location, $q) {
            function success(response) {
                return response;
            }

            function error(response) {
                //if (response.status === 401) {
                //    $location.path('/login');
                //    return $q.reject(response);
                //}
                //else {
                //    return $q.reject(response);
                //}
            }

            return function (promise) {
                return promise.then(success, error);
            };
        }];

    })
    .run(['$rootScope', '$location', 'AuthService',
        function ($rootScope, $location, AuthService) {
            //$rootScope.$on("$routeChangeStart", function (event, next, current) {
            //    if (!AuthService.authorize(next.access) || !AuthService.isLoggedIn())
            //        $location.path('/login');
            //
            //});
            //$rootScope.go = function (path) {
            //    $location.path(path);
            //};
        }])
    .controller('userListCtrl', function ($scope, $http, $modal, $filter, UserServices, $log) {
        UserServices.getUsers.async().then(function (d) {
            $scope.users = d;
        });

        $scope.sortType     = 'name'; // set the default sort type
        $scope.sortReverse  = false;  // set the default sort order

        var modalInstance;
        $scope.openUpdateUser = function (user) {
            $log.info(user);
            modalInstance = $modal.open({
                animation: true,
                templateUrl: 'updateModalContent.html',
                controller: 'UpdateCtrl',
                resolve: {
                    user: function () {
                        return angular.copy(user);
                    },
                    action: function () {
                        return 'Update';
                    }
                },
                backdrop: 'static'
            });

            modalInstance.result.then(function (selectedItem) {
                angular.forEach($scope.users, function (obj) {
                    if (obj.id == selectedItem.id) {
                        $log.info('Found match at: ' + obj.id);
                        for (var k in obj) {
                            if (obj.hasOwnProperty(k) && selectedItem.hasOwnProperty(k)) {
                                obj[k] = selectedItem[k];
                            }
                        }
                    }
                });
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.openCreateUser = function (user) {
            $log.info(user);
            modalInstance = $modal.open({
                animation: true,
                templateUrl: 'updateModalContent.html',
                controller: 'CreateCtrl',
                resolve: {
                    user: function () {
                        return user;
                    },
                    action: function () {
                        return 'Create';
                    }
                },
                backdrop: 'static'
            });

            modalInstance.result.then(function (selectedItem) {
                console.log(selectedItem);
                $scope.users.push(selectedItem);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.openDeleteUser = function (user) {
            $log.info(user);
            modalInstance = $modal.open({
                animation: true,
                templateUrl: 'deleteModalContent.html',
                controller: 'DeleteCtrl',
                resolve: {
                    selectedId: function () {
                        return $scope.selectedId;
                    }
                },
                backdrop: 'static'
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.users = $filter('filter')($scope.users, function (user, id) {
                    if (user.id == $scope.selectedId) {
                        return false;
                    }
                    return true;
                });
                $scope.selectedId = undefined;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.setSelected = function (userId) {
            if ($scope.selectedId == userId) {
                $scope.selectedId = undefined;
            } else {
                $scope.selectedId = userId;
            }
        };
    })
    .controller('UpdateCtrl', function ($scope, $http, $modalInstance, UserServices, user, action,Notification) {
        $scope.user = user;
        $scope.action = action;

        $scope.ok = function () {
            console.log('ok ');
            if ($scope.user.firstname === undefined || $scope.user.firstname.length == 0) {
                $scope.errorMsg = "Firstname cannot be empty!";
                return;
            }
            if ($scope.user.lastname === undefined || $scope.user.lastname.length == 0) {
                $scope.errorMsg = "Lastname cannot be empty!";
                return;
            }
            if ($scope.user.email === undefined || $scope.user.email.length == 0) {
                $scope.errorMsg = "Email cannot be empty!";
                return;
            }

            UserServices.updateUser.async($scope.user).then(function (d) {
                console.log('success ');
                $modalInstance.close(d);
                Notification.success('User was updated');
            }, function (data) {
                $scope.errorMsg = data.message;
            });

        };

        $scope.cancel = function () {
            console.log('cancel : dismiss');
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('CreateCtrl', function ($scope, $http, $modalInstance, UserServices, user, action,Notification) {
        $scope.user = user;
        $scope.action = action;

        $scope.ok = function () {
            if ($scope.user.firstname === undefined || $scope.user.firstname.length == 0) {
                $scope.errorMsg = "Firstname cannot be empty!";
                return;
            }
            if ($scope.user.lastname === undefined || $scope.user.lastname.length == 0) {
                $scope.errorMsg = "Lastname cannot be empty!";
                return;
            }
            if ($scope.user.email === undefined || $scope.user.email.length == 0) {
                $scope.errorMsg = "Email cannot be empty!";
                return;
            }

            UserServices.createUser.async($scope.user).then(function (d) {
                $modalInstance.close(d);
                Notification.success('User created successfully');
            }, function (data) {
                $scope.errorMsg = data.message;
            });

        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('DeleteCtrl', function ($scope, $http, $modalInstance, UserServices, selectedId,Notification) {
        $scope.selectedId = selectedId;

        $scope.ok = function () {
            UserServices.deleteUser.async($scope.selectedId).then(function (d) {
                $modalInstance.close(d);
                Notification.success('User successfully deleted');
            }, function (data) {
                $scope.errorMsg = data.message;
            });

        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('LoginCtrl', function ($scope, $http, $rootScope, $location, $window, $timeout, AUTH_EVENTS, AuthService) {
        AuthService.clearCredentials();

        $scope.login = function (credentials) {
            AuthService.login(credentials).then(function (response) {
                $scope.loginErrorMsg = "";
                $timeout(function () {
                    var userRole = response.data;
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                    $scope.currentUser = userRole;
                    AuthService.setCredentials(userRole);

                });
            }, function (error) {
                $timeout(function () {
                    $scope.loginErrorMsg = error.data;
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                });
            });
        };

        $scope.reset = function () {
            $scope.loginErrorMsg = '';
        };

        $rootScope.$on(AUTH_EVENTS.loginSuccess, function () {
            $location.path('/home');
        });
    })
    .factory('UserServices', function ($http, $q) {
        return {
            getUsers: {
                async: function () {
                    return $http.get('rest/users/list').then(function (response) {
                        return response.data;
                    });
                }
            },
            updateUser: {
                async: function (user) {
                    var def = $q.defer();
                    console.log(user);
                    $http.post('rest/users/update', user).success(function (data, status, headers, config) {
                        def.resolve(data);
                    }).error(function (data) {
                        def.reject(data);
                    });
                    return def.promise;
                }
            },
            createUser: {
                async: function (user) {
                    var def = $q.defer();
                    $http.post('rest/users/create', user).success(function (data, status, headers, config) {
                        def.resolve(data);
                    }).error(function (data) {
                        def.reject(data);
                    });
                    return def.promise;
                }
            },
            deleteUser: {
                async: function (userId) {
                    var def = $q.defer();
                    $http.get('rest/users/delete/' + userId).success(function (data, status, headers, config) {
                        def.resolve(data);
                    }).error(function (data) {
                        def.reject(data);
                    });
                    return def.promise;
                }
            }
        }
    })
    .factory('AuthService', function ($http, $rootScope, $cookies, $httpParamSerializer) {

        var authorizeFunc = function () {
            if ($rootScope.globals === undefined) {
                var globals = {
                    currentUser: {
                        username: $cookies['currentUser'],
                        role: $cookies['currentUserRole']
                    }
                };
                $rootScope.globals = globals;
            }

            if ($rootScope.globals.currentUser) {
                var currentUser = $rootScope.globals.currentUser;
                return currentUser['role'] === 'ADMIN';
            } else {
                return false;
            }
        };
        return {
            authorize: authorizeFunc,
            isLoggedIn: authorizeFunc,
            login: function (credentials) {
                return $http({
                    method: 'POST',
                    url: './login/form',
                    data: $httpParamSerializer({
                        username: credentials.username,
                        password: credentials.password
                    }),
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                });
            },
            setCredentials: function (userRole) {
                $rootScope.globals = {
                    currentUser: userRole
                };

                $cookies.currentUser = userRole.username;
                $cookies.currentUserRole = userRole.role;
            },
            clearCredentials: function () {
                $rootScope.globals = {};
                $cookies.currentUser = '';
                $cookies.currentUserRole = '';
            }
        };
    })
    .directive('formatDate', function () {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                if (ngModel) { // Don't do anything unless we have a model
                    ngModel.$parsers.push(function (value) {
                        return value.valueOf();
                    });
                    ngModel.$formatters.push(function (value) {
                        return new Date(value);
                    });
                }
            }
        };
    })
    .directive('ngEnterKey', function() {
        return function(scope, element, attrs) {

            element.bind("keydown keypress", function(event) {
                console.log('event')
                var keyCode = event.which || event.keyCode;

                // If enter key is pressed
                if (keyCode === 13) {
                    scope.$apply(function() {
                        // Evaluate the expression
                        scope.$eval(attrs.ngEnterKey);
                    });

                    event.preventDefault();
                }
            });
        };
    })
    .constant('AUTH_EVENTS', {
        loginSuccess: 'auth-login-success',
        loginFailed: 'auth-login-failed',
        logoutSuccess: 'auth-logout-success',
        sessionTimeout: 'auth-session-timeout',
        notAuthenticated: 'auth-not-authenticated',
        notAuthorized: 'auth-not-authorized'
    });