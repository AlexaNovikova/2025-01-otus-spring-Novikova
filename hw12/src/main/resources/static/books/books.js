angular.module('app').controller('booksController', function ($scope, $http, $localStorage, $location) {
    const contextPath = 'http://localhost:8189/library';


    $scope.loadPage = function () {
        $http({
            url: contextPath + '/api/v1/books',
            method: 'GET'
        }).then(function (response) {
            $scope.books = response.data;
            console.log($scope.books);
        });
    };

    $scope.deleteBook = function (bookId) {
        $http({
            url: contextPath + '/api/v1/books',
            method: 'DELETE',
            params: {
                id: bookId
            }
        }).then(function (response) {
            $scope.loadPage();
        });
    }

    $scope.showBookInfo = function (bookId) {
        $location.path('/book_info/' + bookId);
    }


    $scope.loadAuthors = function () {
        $http({
            url: contextPath + '/api/v1/authors',
            method: 'GET'
        }).then(function (response) {
            $scope.authors = response.data;
            console.log($scope.authors);
        });
    };

     $scope.loadGenres = function () {
            $http({
                url: contextPath + '/api/v1/genres',
                method: 'GET'
            }).then(function (response) {
                $scope.genres = response.data;
                console.log($scope.genres);
            });
        };

        $scope.addNewBook = function (title, authorId, genreId) {
                  bookToSaveDto = new Object();
                  bookToSaveDto.id = "";
                  bookToSaveDto.title = title;
                  bookToSaveDto.authorId = authorId;
                  bookToSaveDto.genreId = genreId;
                  $http({
                    url: contextPath + '/api/v1/books',
                    method: 'POST',
                    data: JSON.stringify(bookToSaveDto)
                }).then(function (response) {
                    $scope.title ='';
                    $scope.loadPage();
                });
            }

    $scope.loadAuthors();
    $scope.loadGenres();

    $scope.loadPage();
});