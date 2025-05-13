angular.module('app').controller('bookInfoController', function ($scope, $http, $localStorage, $routeParams) {
    const contextPath = 'http://localhost:8189';

    $scope.loadBook = function () {
        $http({
            url: contextPath + '/api/v1/books/' + $routeParams.bookIdParam,
            method: 'GET'
        }).then(function (response) {
            console.log(response.data);
            $scope.book = response.data;
        });
    };

   $scope.addNewComment = function (bookId, text) {
         $http({
             url: contextPath + '/api/v1/books/' + bookId + '/comments',
             method: 'POST',
             params: {
                text: text
             }
         }).then(function (response) {
              $scope.text = '';
              $scope.loadBook();
         });
       };

   $scope.deleteComment = function (bookId, commentId) {
            $http({
                url: contextPath + '/api/v1/books/' + bookId + '/comments/' + commentId,
                method: 'DELETE'
            }).then(function (response) {
                 $scope.loadBook();
            });
        };

    $scope.loadBook();
});