package com.example.AirlineReservationSystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AirlineReservationSystem.service.FlightService;

@RestController
@RequestMapping("/api")
public class FlightRestController {

	@Autowired
	FlightService flightService;

	@PostMapping("/searchFlights")
	public List<Map<String, Object>> searchFlights(@RequestBody Map<String, Object> paramMap) {
		return flightService.searchFlights(paramMap);
	}
	
	@PostMapping("/bookFlight")
	public Map<String, Object> bookFlight(@RequestBody Map<String, Object> paramMap) {
		return flightService.bookFlight(paramMap);
	}
	
	@PostMapping("/getAllBookingDetails")
	public List<Map<String, Object>> getAllBookingDetails(@RequestBody Map<String, Object> paramMap) {
		return flightService.getAllBookingDetails(paramMap);
	}
	
	@PostMapping("/checkin")
	public void checkin(@RequestBody Map<String, Object> paramMap) {
		flightService.checkin(paramMap);
	}
}
