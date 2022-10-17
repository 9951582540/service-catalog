package com.wissen.servicecatalog.service;

import java.util.List;

import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.StatusException;

public interface StatusService {

	public List<Status> getAll() throws StatusException;

	public String updateEmployeeRegisterStatus(Integer employeeId, String employeeStatus) throws EmployeeException;
}
