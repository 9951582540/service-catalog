package com.wissen.servicecatalog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.service.impl.ApplicationRoleServiceImpl;

import io.swagger.annotations.Api;

@Api(tags = "Application Role Service")
@RestController
@RequestMapping("/service-catalog/applicationrole")
@CrossOrigin(origins = "*", maxAge = 3600) 
public class ApplicationRoleController {
	
	Logger logger = LoggerFactory.getLogger(ApplicationRoleController.class);
	
	@Autowired
	ApplicationRoleServiceImpl applicationRoleService;

	@GetMapping("/getAll")
	public List<ApplicationRoleMaster> getAllRoles() throws EmployeeException {
		logger.info("Getting all Roles from Apllication Role Controller");
		return applicationRoleService.getAllRoles();
	}

}
