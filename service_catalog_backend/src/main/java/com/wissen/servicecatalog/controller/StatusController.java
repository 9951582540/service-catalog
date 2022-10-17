package com.wissen.servicecatalog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.StatusException;
import com.wissen.servicecatalog.pojo.StatusResponse;
import com.wissen.servicecatalog.service.impl.StatusServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) 
@Api(tags = "Status Service")
@RequestMapping("/service-catalog/status")
public class StatusController {
	Logger logger = LoggerFactory.getLogger(StatusController.class);
	@Autowired
	StatusServiceImpl statusServiceImpl;

	@PutMapping("/employeeregister/update/{employeeId}/{employeeStatus}")
	public String updateEmployeeRegisterStatus(@PathVariable Integer employeeId, @PathVariable String employeeStatus)
			throws EmployeeException {
		logger.info("Updating Employee Registration Status from Status Controller");
		return statusServiceImpl.updateEmployeeRegisterStatus(employeeId, employeeStatus);
	}

	@GetMapping("/getAll")
	public List<Status> getAll() throws StatusException {

		logger.info("Getting all status from Status Controller");
		return statusServiceImpl.getAll();
	}
	
	@GetMapping("employee/getAll")
	public List<StatusResponse> getAllEmployeeStatus()  {

		logger.info("Getting all employee status from Status Controller");
		return statusServiceImpl.getAllEmployeeStatus();
	}
	

}
