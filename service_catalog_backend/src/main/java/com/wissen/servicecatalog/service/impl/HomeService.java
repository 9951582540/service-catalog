package com.wissen.servicecatalog.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.repository.EmployeeRepository;

@Service
public class HomeService {
	Logger logger = LoggerFactory.getLogger(HomeService.class);
	@Autowired
	EmployeeRepository employeeRepository;

	public String routing(@PathVariable String employeeId) throws EmployeeException {

		logger.info("Routing from Home Screen");
		if (employeeId == null) {
			logger.error("please enter the employee id");
			throw new EmployeeException("please enter the employee id");
		}
		Employee employee = employeeRepository.findByEmployeeId(employeeId);

		if (employee == null) {
			logger.error("Invalid Employee id");
			throw new EmployeeException("Invalid Employee id");
		}
		String role = employee.getApplicationRole().getApplicationName();

		switch (role) {
		case "Employee":
			return "/employee/home";
		case "Project Manager":
			return "/manager/home";
		case "Admin":
			return "/admin/home";
		case "Leadership":
		   return "/senior-manager/home";
		case "Senior Manager":
			   return "/senior-manager/home";
		}
		logger.error("Invalid Employee Role");
		throw new EmployeeException("Invalid Employee Role ");
		

	}

}
