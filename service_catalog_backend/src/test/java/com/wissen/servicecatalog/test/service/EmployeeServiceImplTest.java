package com.wissen.servicecatalog.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.service.impl.EmployeeServiceImpl;


@SpringBootTest
@AutoConfigureMockMvc

public class EmployeeServiceImplTest {
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@MockBean
	EmployeeServiceImpl employeeServiceImpl;
	
	@MockBean
	EmployeeRepository employeeRepository;
	
	private ApplicationRoleMaster applicationRoleMaster;

	Employee employee1=new Employee(1,"ab12","pawan@123.com","java",LocalDateTime.now(),applicationRoleMaster,"active","pvcsa");

	Employee employee2=new Employee(2,"ab13","pawan@123.com","java",LocalDateTime.now(),applicationRoleMaster,"active","pvcsa");
	
	@Test
	public void getEmployeDetails() {
		
	when(employeeRepository.findByEmail("pawan@123.com")).thenReturn(employee1);
		
		assertNotNull(employee1);

		assertEquals("pawan@123.com", employee1.getEmail());
}

	@Test
	public void getAllEmployee() {
		ArrayList<Employee> list = new ArrayList<Employee>();
		list.add(employee1);
		list.add(employee2);
		when(employeeRepository.findAll()).thenReturn(list);
		
		assertNotNull(list);

		assertEquals("pawan@123.com", list.get(1).getEmail());

	}

	@Test
	public void getEmployee() {
		
	when(employeeRepository.findByEmployeeId("ab12")).thenReturn(employee1);
		
		assertNotNull(employee1);

		assertEquals("ab12", employee1.getEmployeeId());
}
	@Test
	public void updateEmployee() throws Exception {
		
		assertNotNull(employee2);
		
		when(employeeRepository.save(employee2)).thenReturn(employee2);
		
		assertEquals("ab13", employee2.getEmployeeId());

	}
	@Test
	public void addEmployee() throws Exception {
	when(employeeRepository.findByEmployeeId(employee1.getEmployeeId())).thenReturn(employee1);
	assertEquals("ab12", employee1.getEmployeeId());
	
	}
	
	@Test
	public void requestUpdate() throws Exception {
       assertNotNull(employee1);
		
		when(employeeRepository.save(employee1)).thenReturn(employee1);
		
		assertEquals("pawan@123.com", employee1.getEmail());
	}

	@Test
	public void getAllManager() {
		ArrayList<Employee> list = new ArrayList<Employee>();
		list.add(employee1);
		list.add(employee2);
		when(employeeRepository.findAll()).thenReturn(list);
		
		assertNotNull(list);

		assertEquals("pawan@123.com", list.get(1).getEmail());

	}
}
