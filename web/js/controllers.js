'use strict';

angular.module('wishlist.controllers', [])

    .controller('GreetingController', function ($scope, $state, GreetingService) {
        $scope.greeting = GreetingService.get();
    })
    .controller('LoginController', function ($scope, $state, $location, Restangular) {
        
        $scope.login = function(){
            // alert('username=' + $scope.credentials.username + '&password=' + $scope.credentials.password);

            Restangular.all('login').post('username=' + $scope.credentials.username
                + '&password=' + $scope.credentials.password).then(function(response){
                var resp = response;
                console.log(resp);
                $location.path("/friends");

            }, function(resp){
                console.log(resp);
            });
            // Restangular.all('users').getList().then(function(response){
            //     $scope.users = response;
            //     console.log($scope.users);
            // })

        };
        $scope.logout = function(){
            // alert('username=' + $scope.credentials.username + '&password=' + $scope.credentials.password);

            Restangular.all('logout').post().then(function(response){
                var resp = response;
                console.log(resp);
            }, function(resp){
                console.log(resp);
            });
            // Restangular.all('users').getList().then(function(response){
            //     $scope.users = response;
            //     console.log($scope.users);
            // })



        };

        $scope.usersM = function(){
            // alert('username=' + $scope.credentials.username + '&password=' + $scope.credentials.password);

            Restangular.all('users').getList().then(function(response){
                $scope.users = response;
                console.log($scope.users);
            }, function(resp){
                console.log(resp);
            })



        };


    })
    .controller('FriendController', function ($scope, $state, Restangular) {
        var users = Restangular.all('friends').getList().then(function(resp){
            $scope.friends = resp;
            console.log($scope.friends);
        });

    });

