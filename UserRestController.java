package com.example.AirlineReservationSystem.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AirlineReservationSystem.service.UserService;

@RestController
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	UserService userService;

	@PostMapping("/login")
	public boolean login(@RequestBody Map<String, Object> paramMap) {
		return userService.login(paramMap);
	}
	
	@PostMapping("/register")
	public Map<String, Object> register(@RequestBody Map<String, Object> paramMap) {
		return userService.register(paramMap);
	}
}
