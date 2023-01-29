var app = angular.module("registerApp", []);

app.controller('registerController', ['$scope', 'RegisterService',
	function($scope, RegisterService) {
		$scope.addUser = function() {
			if ($scope.user === undefined || !$scope.user.firstName || !$scope.user.lastName || !$scope.user.mobileNumber ||
				!$scope.user.gender || !$scope.user.userName || !$scope.user.password) {
				$scope.errorMessage = 'Please fill all the details';
				$scope.message = '';
			} else {
				RegisterService.addUser($scope.user.firstName, $scope.user.lastName, $scope.user.mobileNumber,
					$scope.user.gender, $scope.user.userName, $scope.user.password)
					.then(function success(response) {
						if (response['data']['error'] === 'user exist') {
							$scope.errorMessage = 'User already exist';
							$scope.message = '';
						} else if (response['data']['error'] === 'insert failed') {
							$scope.errorMessage = 'Error occured during registration. Try again';
							$scope.message = '';
						} else {
							$scope.message = 'Registration Successful! Click login ';
							$scope.user = {};
							$scope.errorMessage = '';
						}
					},
						function error() {
							$scope.errorMessage = 'Error occured during registration. Try again';
							$scope.user = {};
							$scope.message = '';
						});
			}
		};
	}]);

app.service("RegisterService", ['$http', function($http) {
	this.addUser = function addUser(firstName, lastName, mobileNumber, gender, userName, password) {
		return $http({
			method: 'POST',
			url: 'api/register',
			data: {
				firstname: firstName,
				lastname: lastName,
				contact_no: mobileNumber,
				gender: gender,
				username: userName,
				password: password
			}
		});
	}
}]);