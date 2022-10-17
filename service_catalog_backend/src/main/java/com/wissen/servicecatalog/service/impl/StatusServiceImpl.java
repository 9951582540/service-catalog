package com.wissen.servicecatalog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.StatusException;
import com.wissen.servicecatalog.pojo.StatusResponse;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.repository.StatusRepository;
import com.wissen.servicecatalog.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService {
	Logger logger = LoggerFactory.getLogger(StatusServiceImpl.class);
	@Autowired
	StatusRepository statusRepository;

	@Autowired
	EmployeeRepository employeeRegisterRepository;

	@Autowired
	EmployeeMasterRepository employeeRepository;

	@Override
	public List<Status> getAll() throws StatusException {
		logger.info("Getting all from Status Service");
		List<Status> list = statusRepository.findAll();
		if (list.isEmpty()) {
			logger.error("No records found");
			throw new StatusException("No records found");
		}
		return list;
	}

	@Override
	public String updateEmployeeRegisterStatus(Integer employeeId, String employeeStatus) throws EmployeeException {
		logger.info("Updating Employee Register status from Status Service");
		Employee employee = employeeRegisterRepository.findByEmployeeId(employeeId + "");

		if (employee == null) {
			logger.error("Invalid employee Id");
			throw new EmployeeException("Invalid employee Id");
		}
		if (employee.getStatus().equalsIgnoreCase(employeeStatus)) {
			logger.error("Status is already updated");
			throw new EmployeeException("Status is already updated");
		}
		employee.setStatus(employeeStatus);
		employeeRegisterRepository.save(employee);

		return "Status is updated successfully";
	}

	public List<StatusResponse> getAllEmployeeStatus() {

		return employeeRegisterRepository.findAll().stream().map(
				i -> new StatusResponse(i.getEmployeeId(), i.getApplicationRole().getApplicationName(), i.getStatus()))
				.collect(Collectors.toList());
	}
}
