package com.example.AirlineReservationSystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AirlineReservationSystem.dao.UserDao;

@Service
public class UserService {	
	
	@Autowired
	UserDao userDao;

	public boolean login(Map<String, Object> paramMap) {
		List<Map<String, Object>> userList = userDao.login(paramMap);
		if(userList.isEmpty()) {
			return false;
		}
		return true;
	}

	public Map<String, Object> register(Map<String, Object> paramMap) {
		Map<String, Object> returnMap = new HashMap<>();
		List<Map<String, Object>> userList = userDao.checkUserAvailable(paramMap);
		if(!userList.isEmpty()) {
			returnMap.put("error", "user exist");
		} else if (!userDao.register(paramMap)){
			returnMap.put("error", "insert failed");
		}
		return returnMap;
	}
}
