package com.example.AirlineReservationSystem.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<Map<String, Object>> login(Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForList("select * from user where username = :username and password = :password;", paramMap);
	}

	public boolean register(Map<String, Object> paramMap) {
		try {
			namedParameterJdbcTemplate.update(
				    "INSERT INTO user (firstname, lastname, contact_no, gender, username, password) VALUES "
				    + "(:firstname, :lastname, :contact_no, :gender, :username, :password)",
				    paramMap
				);
		} catch (Exception exception){
			return false;
		}
		return true;
	}

	public List<Map<String, Object>> checkUserAvailable(Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.queryForList("select * from user where username = :username;", paramMap);
	}
}
