var app = angular.module("checkinApp", []);

app.controller('checkinController', ['$scope', 'CheckinService', '$window', '$http',
	function($scope, CheckinService, $window, $http) {

		$scope.checkinOnLoad = function() {
			$scope.username = $window.sessionStorage.getItem('username');
			$scope.isAuthenticated = $window.sessionStorage.getItem('isAuthenticated');
			$scope.isCheckinFetchResult = false;
		};

		$scope.logout = function() {
			$window.sessionStorage.setItem('username', '');
			$window.sessionStorage.setItem('isAuthenticated', false);
			$window.location.href = '/login';
		}

		$scope.fetchCheckinData = function() {
			if ($scope.checkin === undefined || !$scope.checkin.bookingId) {
				$scope.errorMessage = 'Please fill Booking ID';
				$scope.message = '';
				$scope.isCheckinFetchResult = false;
			} else {
				CheckinService.fetchCheckinData($scope.checkin.bookingId)
					.then(function success(response) {
						if (response.data.length <= 0) {
							$scope.isCheckinFetchResult = false;
							$scope.errorMessage = 'No Records Found!';
							$scope.message = '';
						} else {
							$scope.bookingRecord = response.data;
							$scope.errorMessage = '';
							$scope.message = '';
							$scope.isCheckinFetchResult = true;
						}

					},
						function error() {
							$scope.errorMessage = 'Error while getting the flights. Please try again later!';
							$scope.message = '';
							$scope.isCheckinFetchResult = false;
						});
			}
		};

		$scope.checkinFlight = function checkinFlight(bookingId, passengerId) {
			$scope.passengerFromUI = {
				bookingId: bookingId,
				passengerId: passengerId
			};
			$http({
				method: 'POST',
				url: 'api/checkin',
				data: $scope.passengerFromUI
			}).then(function success() {
				$window.location.href = '/checkin';
			}, function error() {

			});
		};

	}]);


app.service("CheckinService", ['$http', function($http) {
	this.fetchCheckinData = function fetchCheckinData(bookingId) {
		return $http({
			method: 'POST',
			url: 'api/getAllBookingDetails',
			data: {
				bookingId: bookingId
			}
		});
	}
}]);

