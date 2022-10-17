package com.wissen.servicecatalog.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.repository.EmployeeRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
	Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
		logger.info("Loading User by Username ftom Custom User Details Service");
		Employee employee = employeeRepository.findByEmployeeId(employeeId);

		if (employee == null) {
			logger.error("EmployeeId Not found");
			throw new UsernameNotFoundException("EmployeeId Not found" + employeeId);		
		}
		return new User(employee.getEmployeeId(), employee.getPassword(), new ArrayList<>());
	}
}