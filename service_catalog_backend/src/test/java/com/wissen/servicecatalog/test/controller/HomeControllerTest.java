package com.wissen.servicecatalog.test.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.service.impl.HomeService;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

	
	@MockBean
	AuthenticationManager authenticationManager;

	@MockBean
	HomeService homeService;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;
    
	String homeJson = null;

	private ApplicationRoleMaster applicationRoleMaster;
	
    
	
	Employee employee1=new Employee(1,"5872","pawan@123.com","java",LocalDateTime.now(),applicationRoleMaster,"active","pvcsa");

	Employee employee2=new Employee(2,"5872","pawan@123.com","java",LocalDateTime.now(),applicationRoleMaster,"active","pvcsa");
	
	
	
	@Test
	public void getRole() throws Exception {
		when(homeService.routing(ArgumentMatchers.any())).thenReturn("5872");

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/homerouting//role/5872}");

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

		assertEquals("5872",employee1.getEmployeeId());

	}
	
}
