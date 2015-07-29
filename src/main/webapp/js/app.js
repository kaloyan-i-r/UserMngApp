angular.module('westernacherDemoApp', [])
  .controller('test', function($scope,$http,$log) {
    $http.get("/users/list").success(function(data) {
      $scope.users = data;
    });
    $log.info($scope.users);
});