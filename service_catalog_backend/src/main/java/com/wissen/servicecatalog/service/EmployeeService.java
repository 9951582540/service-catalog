package com.wissen.servicecatalog.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.EmployeUpdateRequest;
import com.wissen.servicecatalog.pojo.EmployeeRequest;
import com.wissen.servicecatalog.pojo.EmployeeResponse;
import com.wissen.servicecatalog.pojo.ManagerResponse;

public interface EmployeeService {

	public EmployeeMaster addEmployee(EmployeeRequest employee) throws EmployeeException;

	public List<EmployeeMaster> updateEmployee( List<EmployeeRequest> employeeRequest)
			throws EmployeeException;

	public List<EmployeeResponse> getAllEmployee() throws EmployeeException;

	public EmployeeResponse getEmployee(Integer employeeId) throws EmployeeException;

	public String updateByEmployee(EmployeUpdateRequest employe, Integer employeId) throws EmployeeException;

	public EmployeeMaster getEmployeDetails(String email) throws EmployeeException;

	public List<ManagerResponse> getAllManager() throws EmployeeException;

	public String requestUpdate(Integer employeeId, String newName, String newMail)
			throws EmployeeException, MessagingException, IOException, SettingException;

	public ResponseEntity<Object> importEmployeeDetails(MultipartFile file) throws FileNotFoundException, EmployeeException, MessagingException, SettingException, IOException;

	public String autoGeneratePassword();

}