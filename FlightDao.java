package com.example.AirlineReservationSystem.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

@Service
public class FlightDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<Map<String, Object>> searchFlights(Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForList("select * from flight where origin = :origin and destination = :destination;", paramMap);
	}

	public Map<String, Object> getBookingId(Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForMap("select max(bookingId) as bookingId from booking_details;", paramMap);
	}

	public void saveBookingDetails(Map<String, Object> paramMap) {
		namedParameterJdbcTemplate.update(
			    "INSERT INTO booking_details (bookingId, flightNumber, bookingDate, flightDate, fare, status) VALUES "
			    + "(:bookingId, :flightNumber, :bookingDate, :flightDate, :fare, :status)",
			    paramMap
			);
	}

	public void saveBookingPassengers(Map<String, Object> paramMap) {
		List<Map<String, Object>> passengersList = new ArrayList<>();
		List<Map<String, Object>> list = (List<Map<String, Object>>) paramMap.get("passengers");
		for(Map<String, Object> map : list) {
			if(Objects.nonNull(map.get("firstName")) && Objects.nonNull(map.get("lastName")) && Objects.nonNull(map.get("gender")) && 
					Objects.nonNull(map.get("mobileNumber"))) {
				passengersList.add(map);
			}
		}
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(passengersList.toArray());
		namedParameterJdbcTemplate.batchUpdate("INSERT INTO booking_passengers (bookingId, firstName, lastName, gender, mobileNumber) VALUES "
				+ "(" + paramMap.get("bookingId") + ", :firstName, :lastName, :gender, :mobileNumber)",
				batch);
	}

	public Map<String, Object> getBookingDetails(Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForMap("select booking_details.*, origin, destination, flightTime from booking_details "
				+ "join flight on booking_details.flightNumber = flight.flightNumber "
				+ "where bookingId = :bookingId;", paramMap);
	}

	public List<Map<String, Object>> getAllBookingDetails(Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForList("select booking_passengers.* from booking_details "
				+ "join booking_passengers on booking_details.bookingId = booking_passengers.bookingId "
				+ "where booking_details.bookingId = :bookingId;", paramMap);
	}

	public Map<String, Object> getSeatNumber(Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForMap("select max(seatNumber) as seatNumber from booking_details "
				+ "join (select flightNumber, flightDate from booking_details where bookingId = :bookingId) as flight "
				+ "on booking_details.flightNumber = flight.flightNumber and booking_details.flightDate = flight.flightDate "
				+ "join booking_passengers on booking_details.bookingId = booking_passengers.bookingId;", paramMap);
	}

	public void checkin(Map<String, Object> paramMap) {
		namedParameterJdbcTemplate.update("update booking_passengers set seatNumber = :seatNumber where passengerId = :passengerId;", paramMap);
	}
}
