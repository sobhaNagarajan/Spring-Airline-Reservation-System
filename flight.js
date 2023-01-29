var app = angular.module("searchFlightsApp", []);

app.controller('searchFlightsController', ['$scope', 'SearchFlightsService', '$window', '$http',
	function($scope, SearchFlightsService, $window, $http) {

		$scope.flightOnLoad = function() {
			$scope.username = $window.sessionStorage.getItem('username');
			$scope.isAuthenticated = $window.sessionStorage.getItem('isAuthenticated');
			$scope.isSearchEnabled = true;
		};

		$scope.logout = function() {
			$window.sessionStorage.setItem('username', '');
			$window.sessionStorage.setItem('isAuthenticated', false);
			$window.location.href = '/login';
		}

		$scope.flights = [];
		$scope.IsVisible = false;
		$scope.searchFlights = function() {
			$scope.IsVisible = false;
			if ($scope.searchcriteria === undefined || !$scope.searchcriteria.origin || !$scope.searchcriteria.destination || !$scope.searchcriteria.flightDate ||
				!$scope.searchcriteria.count) {
				$scope.errorMessage = 'Please fill all the details';
				$scope.message = '';
			}
			else {
				SearchFlightsService.searchFlights($scope.searchcriteria.origin, $scope.searchcriteria.destination, $scope.searchcriteria.flightDate, $scope.searchcriteria.count)
					.then(function success(response) {
						if (response.data.length <= 0) {
							$scope.IsVisible = false;
							$scope.errorMessage = 'No Records Found!';
							$scope.message = '';
						} else {
							$scope.flights = response.data;
							$scope.errorMessage = '';
							$scope.message = '';
							$scope.IsVisible = true;
						}

					},
						function error() {
							$scope.errorMessage = 'Error while getting the flights. Please try again later!';
							$scope.message = '';
						});
			};
		}




		$scope.isBookingEnabled = false;
		$scope.bookFlight = function(nameOfAirline, flightNumber, origin, destination, flightDate, flightTime, fare) {
			$scope.isBookingEnabled = true;
			$scope.isSearchEnabled = false;
			$scope.bookFlight.nameOfAirline = nameOfAirline;
			$scope.bookFlight.flightNumber = flightNumber;
			$scope.bookFlight.origin = origin;
			$scope.bookFlight.destination = destination;
			$scope.bookFlight.flightDate = flightDate;
			$scope.bookFlight.flightTime = flightTime;
			$scope.bookFlight.fare = fare;
			for (var i = parseInt($scope.searchcriteria.count) + 1; i <= 4; i++) {
				$('#passenger-' + i + ' :input').attr('disabled', true);
			}
		};

		$scope.bookFlightWithPassenger = function(bookFlight, passenger1, passenger2, passenger3, passenger4) {
			var errorFlag = false;
			for (var i = 1; i <= parseInt($scope.searchcriteria.count); i++) {
				if (this["passenger" + i] === undefined || this["passenger" + i].firstName === undefined || !this["passenger" + i].firstName ||
					this["passenger" + i].lastName === undefined || !this["passenger" + i].lastName || this["passenger" + i].gender === undefined ||
					!this["passenger" + i].gender || this["passenger" + i].mobileNumber === undefined || !this["passenger" + i].mobileNumber) {
					errorFlag = true;
				}
			}
			if (errorFlag) {
				$scope.errorMessage = 'Please fill all the details';
				$scope.message = '';
			} else {
				$scope.passengers = [];
				$scope.passengers.push({
					firstName: passenger1.firstName,
					lastName: passenger1.lastName,
					gender: passenger1.gender,
					mobileNumber: passenger1.mobileNumber
				},
					{
						firstName: passenger2.firstName,
						lastName: passenger2.lastName,
						gender: passenger2.gender,
						mobileNumber: passenger2.mobileNumber
					},
					{
						firstName: passenger3.firstName,
						lastName: passenger3.lastName,
						gender: passenger3.gender,
						mobileNumber: passenger3.mobileNumber
					},
					{
						firstName: passenger4.firstName,
						lastName: passenger4.lastName,
						gender: passenger4.gender,
						mobileNumber: passenger4.mobileNumber
					});
				bookFlight.passengers = $scope.passengers;
				//			$scope.isBookingEnabled = false;
				//			$scope.isSearchEnabled = false;
				//			$scope.isBookingConfirmEnabled = true;
				//			$scope.bookingDetails = '';

				$http({
					method: 'POST',
					url: 'api/bookFlight',
					data: {
						flightNumber: bookFlight.flightNumber,
						origin: bookFlight.origin,
						destination: bookFlight.destination,
						fare: bookFlight.fare,
						flightDate: bookFlight.flightDate,
						flightTime: bookFlight.flightTime,
						passengers: bookFlight.passengers
					}
				}).then(function success(response) {
					$scope.isBookingEnabled = false;
					$scope.isSearchEnabled = false;
					$scope.isBookingConfirmEnabled = true;
					$scope.bookingDetails = '';
					$scope.bookingDetails = response.data;
				},
					function error() {
						$scope.errorMessage = 'Error while getting the flights. Please try again later!';
						$scope.message = '';
					});
			}

		};

	}]);


app.service("SearchFlightsService", ['$http', function($http) {
	this.searchFlights = function searchFlights(origin, destination, flightDate, count) {
		return $http({
			method: 'POST',
			url: 'api/searchFlights',
			data: {
				origin: origin,
				destination: destination,
				flightDate: flightDate.getFullYear() + "-" + ((flightDate.getMonth() + 1) < 10 ? '0' + (flightDate.getMonth() + 1) : (flightDate.getMonth() + 1)) + "-" + (flightDate.getDate() < 10 ? '0'+ flightDate.getDate() : flightDate.getDate()),
				count: count
			}
		});
	}
}]);

