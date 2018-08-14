package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.User;
import com.example.demo.dao.UserMapper;

@RestController
@RequestMapping({ "/user" })
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class UserController {
	@Autowired
	UserMapper userMapper;

	@RequestMapping(value="/user", produces = "application/json;charset=UTF-8")

	@ResponseBody
	
	public User getUserInfoByName(String name) {

	User user = userMapper.findUserByName(name);

	return user;

	}
}
