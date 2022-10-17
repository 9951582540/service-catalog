package com.wissen.servicecatalog.service;

import java.util.List;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.exception.EmployeeException;

public interface ApplicationRoleMasterService {

	public List<ApplicationRoleMaster> getAllRoles() throws EmployeeException;
	
}
