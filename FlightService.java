package com.example.AirlineReservationSystem.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
//import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AirlineReservationSystem.dao.FlightDao;

@Service
public class FlightService {	
	
	@Autowired
	FlightDao flightDao;

	public List<Map<String, Object>> searchFlights(Map<String, Object> paramMap) {
		List<Map<String, Object>> flightList = flightDao.searchFlights(paramMap);
		LocalDate flightDate = LocalDate.parse(paramMap.get("flightDate").toString());
		for(Map<String, Object> flightMap : flightList) {
			flightMap.put("flightDate", flightDate);
			flightMap.put("fare", Integer.parseInt(flightMap.get("fare").toString()) * Integer.parseInt(paramMap.get("count").toString()));
		}
		return flightList;
	}

	@Transactional
	public Map<String, Object> bookFlight(Map<String, Object> paramMap) {
		Map<String, Object> bookingIdMap = flightDao.getBookingId(paramMap);
		int bookingId = 1;
		if(Objects.nonNull(bookingIdMap.get("bookingId"))) {
			bookingId = Integer.parseInt(bookingIdMap.get("bookingId").toString()) + 1;
		}
		paramMap.put("bookingId", bookingId);
		paramMap.put("status", "Booked");
		paramMap.put("bookingDate", LocalDate.now());
		flightDao.saveBookingDetails(paramMap);
		flightDao.saveBookingPassengers(paramMap);
		return flightDao.getBookingDetails(paramMap);
	}

	public List<Map<String, Object>> getAllBookingDetails(Map<String, Object> paramMap) {
		return flightDao.getAllBookingDetails(paramMap);
	}

	public void checkin(Map<String, Object> paramMap) {
		Map<String, Object> seatNumberMap = flightDao.getSeatNumber(paramMap);
		int seatNumber = 1;
		if(Objects.nonNull(seatNumberMap.get("seatNumber"))) {
			seatNumber = Integer.parseInt(seatNumberMap.get("seatNumber").toString()) + 1;
		}
		paramMap.put("seatNumber", seatNumber);
		flightDao.checkin(paramMap);
	}
}
