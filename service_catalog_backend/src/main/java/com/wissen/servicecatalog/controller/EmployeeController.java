package com.wissen.servicecatalog.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.EmployeUpdateRequest;
import com.wissen.servicecatalog.pojo.EmployeeRequest;
import com.wissen.servicecatalog.pojo.EmployeeResponse;
import com.wissen.servicecatalog.pojo.ManagerResponse;
import com.wissen.servicecatalog.pojo.ResourceDetails;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.service.impl.EmployeeServiceImpl;

import io.swagger.annotations.Api;

@Api(tags = "Employee Service")
@RestController
@RequestMapping("/service-catalog/employee")
@CrossOrigin(origins = "*", maxAge = 3600)  
public class EmployeeController {
	Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeServiceImpl employeeService;

	@Autowired
	EmployeeMasterRepository employeeRepository;

	
	@PostMapping("/add")
	public EmployeeMaster addEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) throws EmployeeException {
		logger.info("Adding the Employee from Employee Controller");
		return employeeService.addEmployee(employeeRequest);
	}

	@GetMapping("/get/{employeeId}")
	public EmployeeResponse getEmployee(@PathVariable("employeeId") Integer employeeId) throws EmployeeException {
		logger.info("Getting the Employee from Employee Controller");
		return employeeService.getEmployee(employeeId);
	}

	@GetMapping("/getAll")
	public List<EmployeeResponse> getAllEmployee() throws EmployeeException {
		logger.info("Getting All the Employee from Employee Controller");
		return employeeService.getAllEmployee();
	}

	@GetMapping("/manager/getAll")
	public List<ManagerResponse> getAllManager() throws EmployeeException {
		logger.info("Getting All the Manager from Employee Controller");
		return employeeService.getAllManager();
	}

	@PatchMapping("/update")
	public List<EmployeeMaster> updateEmployee(
			@RequestBody @Valid List<EmployeeRequest> employeeRequestList) throws EmployeeException {
		logger.info("Update Employee Id from Employee Controller");
		return employeeService.updateEmployee(employeeRequestList);
	}

	@PatchMapping("/updateDetails/{employeId}")
	public String updateEmployeDetails(@PathVariable("employeId") Integer employeId,
			@RequestBody @Valid EmployeUpdateRequest employe) throws EmployeeException {
		logger.info("Update Details through Employee ID from Employee Controller");
		return employeeService.updateByEmployee(employe, employeId);
	}

	@PatchMapping("/requestUpdate/{employeeId}/{newName}/{newMail}")
	public String requestUpdate(@PathVariable("employeeId") Integer employeeId,
			@Valid @PathVariable("newName") String newName, @PathVariable("newMail") @Email String newMail)
			throws EmployeeException, MessagingException, IOException, SettingException {
		logger.info("Request Update from Employee Controller");
		return employeeService.requestUpdate(employeeId, newName, newMail);
	}

	@PostMapping("/upload")
	public ResponseEntity<Object> importEmployeeDetails(@Valid @RequestParam MultipartFile file)
			throws EmployeeException, MessagingException, SettingException, IOException {
		logger.info("Upload from Employee Controller");
	
		return employeeService.importEmployeeDetails(file);
	}

	@GetMapping("/download")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		logger.info("Download the employee master data from Employee Controller");
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headervalue = "attachment; filename=Employee_info.xlsx";

		response.setHeader(headerKey, headervalue);
		List<EmployeeMaster> listEmployee = employeeRepository.findAll();
		EmployeeServiceImpl employee = new EmployeeServiceImpl(listEmployee);
		employee.export(response);

	}

	@GetMapping("/resource-details/getAll")
	public Set<ResourceDetails> getAllResourceDetails() throws EmployeeException {
		logger.info("Getting all resource details from Employee Controller");
		return employeeService.getAllResourceDetails();
	}

}