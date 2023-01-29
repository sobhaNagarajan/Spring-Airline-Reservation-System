var app = angular.module("loginApp", []);

app.controller('loginController', ['$scope', 'LoginService', '$window',
	function($scope, LoginService, $window) {
		$scope.login = function() {
			if ($scope.user === undefined || !$scope.user.username || !$scope.user.password) {
				$scope.errorMessage = 'Please fill all the details';
			} else {
				LoginService.login($scope.user.username, $scope.user.password)
					.then(function success(response) {
						if (response['data']) {
							$window.sessionStorage.setItem('username', $scope.user.username);
							$window.sessionStorage.setItem('isAuthenticated', true);
							$window.location.href = '/';
						} else {
							$scope.errorMessage = 'Error while login';
							$window.sessionStorage.setItem('isAuthenticated', false);
						}
					},
						function error() {
							$scope.errorMessage = 'Error while login';
							$window.sessionStorage.setItem('isAuthenticated', false);
						});
			}
		};
	}]);

app.service("LoginService", ['$http', function($http) {
	this.login = function login(username, password) {
		var data = {
			username: username,
			password: password
		};
		return $http.post('api/login', data);
	}
}]);