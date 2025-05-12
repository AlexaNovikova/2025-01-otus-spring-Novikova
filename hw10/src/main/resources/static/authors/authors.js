angular.module('app').controller('authorsController', function ($scope, $http, $localStorage, $location) {
    const contextPath = 'http://localhost:8189/library';


    $scope.loadPage = function () {
        $http({
            url: contextPath + '/api/v1/authors',
            method: 'GET'
        }).then(function (response) {
            $scope.authors = response.data;
            console.log($scope.authors);
        });
    };

    $scope.deleteAuthor = function (authorId) {
        $http({
            url: contextPath + '/api/v1/authors',
            method: 'DELETE',
            params: {
                id: authorId
            }
        }).then(function (response) {
            $scope.loadPage();
        });
    }

     $scope.addNewAuthor = function (fullName) {
          authorDto=new Object();
          authorDto.id = "";
          authorDto.fullName = fullName;
              $http({
                  url: contextPath + '/api/v1/authors',
                  method: 'POST',
                  data: JSON.stringify(authorDto)
              }).then(function successCallback (response) {
                     $scope.fullName = '';
                     $scope.loadPage();
              }, function(errorResponse, status)  {
                     console.log(errorResponse);
                     console.log(status);
                     alert('Ошибка: ' + errorResponse);
                 });
     };

    $scope.loadPage();
});