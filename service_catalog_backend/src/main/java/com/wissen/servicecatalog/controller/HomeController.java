package com.wissen.servicecatalog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.service.impl.HomeService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/service-catalog/homerouting")
@CrossOrigin(origins = "*", maxAge = 3600) 
@Api(tags = "Home Routing Service")
public class HomeController {

	Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	HomeService homeService;

	@GetMapping("/role/{employeeId}")
	public String getRole(@PathVariable String employeeId) throws EmployeeException {
		logger.info("Getting roles thorugh Employee Id from Home Controller");
		return homeService.routing(employeeId);

	}

}
