package com.wissen.servicecatalog.test.service;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.service.impl.HomeService;



@SpringBootTest
@AutoConfigureMockMvc
public class HomeServiceImplTest {

	@MockBean
	EmployeeRepository employeeRepository;

	
	@MockBean
	HomeService homeService;
	
	
	@MockBean
	AuthenticationManager authenticationManager;


	private ApplicationRoleMaster applicationRoleMaster;
	

	
	@Test
	public void routing() throws EmployeeException{
		Employee employee=new Employee(1,"5873","pawan@123.com","java",LocalDateTime.now(),applicationRoleMaster,"active","pvcsa");
		
		when (employeeRepository.findByEmployeeId("5873")).thenReturn(employee);
		
		assertEquals("5873",employee.getEmployeeId());
		
		
	}
}