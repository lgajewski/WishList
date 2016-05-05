'use strict';

controllers.controller('GiftController', function ($scope, $http, GiftService) {
    $scope.gifts = {};
    $scope.gift = {};

    $scope.getGifts = function(){
        GiftService.getGifts().then(function (gifts) {
            console.log(gifts);
            $scope.gifts = gifts;
        }, function (err) {
            console.log("getGifts ERROR ", err);
        });
    };

    $scope.getGift = function () {
        GiftService.getGift().then(function (gift) {
            console.log(gift);
            $scope.gift = gift;
        }, function (err) {
            console.log("getGift ERROR ", err);
        });
    };

    $scope.addGift = function (gift) {
        GiftService.addGift(gift).then(function () {
            $scope.gift = '';
        }, function (err) {
            console.log("addGift ERROR ", err);
        });
    };

    $scope.editGift = function (gift) {
        GiftService.editGift(gift).then(function (gift) {
            console.log(gift);
            $scope.gift = gift;
        }, function (err) {
            console.log("editGift ERROR ", err);
        });
    };

    $scope.removeGift = function (gift) {
        GiftService.removeGift(gift).then(function () {
            //TODO
            //console.log(gift);
            //$scope.gift = gift;
        }, function (err) {
            console.log("deleteGift ERROR ", err);
        });
    };

});

//controllers.controller('UserController', ['$scope', '$dialogs', 'UsersService',
//    function ($scope, $dialogs, UsersService) {
//
//        $scope.data = {};
//
//        getUsers();
//
//        var getUsers = function () {
//
//            UsersService.getUsers(function (successResult) {
//                $scope.users = successResult;
//            },
//            function (errorResult) {
//                $dialogs.error("Error occurred!", errorResult.data.error);
//            }
//        );
//    };
//}]);