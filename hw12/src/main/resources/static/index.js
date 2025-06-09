(function ($localStorage) {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngStorage', 'ngCookies'])
        .config(config);

    function config($routeProvider, $httpProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'home/home.html',
                controller: 'homeController'
            })
            .when('/books', {
                templateUrl: 'books/books.html',
                controller: 'booksController'
            })
            .when('/authors', {
                templateUrl: 'authors/authors.html',
                controller: 'authorsController'
            })
            .when('/genres', {
                templateUrl: 'genres/genres.html',
                controller: 'genresController'
            })
            .when('/book_info/:bookIdParam', {
                 templateUrl: 'book_info/book_info.html',
                 controller: 'bookInfoController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }
    })();


angular.module('app').controller('indexController', function ($scope, $http, $localStorage, $location, $cookies) {
    const contextPath = 'http://localhost:8189/library';

 });