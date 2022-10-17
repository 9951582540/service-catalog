package com.wissen.servicecatalog.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;
import com.wissen.servicecatalog.service.ApplicationRoleMasterService;

@Service
public class ApplicationRoleServiceImpl implements ApplicationRoleMasterService {
	Logger logger = LoggerFactory.getLogger(ApplicationRoleServiceImpl.class);
	
	@Autowired
	ApplicationRoleMasterRepository applicationRoleMasterRepository;
	
	@Override
	public List<ApplicationRoleMaster> getAllRoles() throws EmployeeException
    { 
		logger.info("Getting all Roles from Application Role Service");
		List<ApplicationRoleMaster> roles=	applicationRoleMasterRepository.findAll();
		if(roles.isEmpty()) {
			logger.error("No roles added, please add the roles");
			throw new EmployeeException("No roles added, please add the roles");
		}
		return roles;
    }
}
