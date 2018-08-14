package com.example.demo.dao;

import com.example.demo.bean.User;

public interface UserMapper {

	User findUserByName(String name);
}
