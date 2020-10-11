package com.different.webpage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.different.webpage.dao.MysqlDao;
import com.different.webpage.dto.UserDto;

@Service
public class UserService {
	@Autowired
	MysqlDao dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto user = dao.getUserById(username);
		
		if(null == user) {
			throw new UsernameNotFoundException("User Not Found");
		}
		
		return user;
	}
	
}
