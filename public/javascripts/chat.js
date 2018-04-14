var app = angular.module('chatApp', ['ngMaterial']);

app.controller('chatController', function ($scope) {
    $scope.messages = [
        {
            "sender": "USER",
            "text": "Hello"
		},
        {
            "sender": "BOT",
            "text": "Hi, How can I help?"
		},
        {
            "sender": "USER",
            "text": "Get information about Marvel"
		},
        {
            "sender": "BOT",
            "text": "Searching for Marvel"
		}
	];

});