angular.module('app').controller('genresController', function ($scope, $http, $localStorage, $location) {
    const contextPath = 'http://localhost:8189/library';


    $scope.loadPage = function () {
        $http({
            url: contextPath + '/api/v1/genres',
            method: 'GET'
        }).then(function (response) {
            $scope.genres = response.data;
            console.log($scope.genres);
        });
    };

    $scope.deleteGenre = function (genreId) {
        $http({
            url: contextPath + '/api/v1/genres',
            method: 'DELETE',
            params: {
                id: genreId
            }
        }).then(function (response) {
            $scope.loadPage();
        });
    }

  $scope.addNewGenre = function (name) {
          genreDto=new Object();
          genreDto.id = "";
          genreDto.name = name;
              $http({
                  url: contextPath + '/api/v1/genres',
                  method: 'POST',
                  data: JSON.stringify(genreDto)
              }).then(function (response) {
                     $scope.name = '';
                     $scope.loadPage();
              });
     };

    $scope.loadPage();
});