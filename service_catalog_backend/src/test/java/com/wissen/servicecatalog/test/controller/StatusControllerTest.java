package com.wissen.servicecatalog.test.controller;



import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.service.impl.StatusServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusControllerTest {
	
	
	@MockBean
	AuthenticationManager authenticationManager;
	

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@MockBean
	StatusServiceImpl statusServiceImpl;
	
	String StatusJson =null;
	
	Status status=new Status(1,"active");
	
	@org.junit.jupiter.api.Test
    public void getallStatus()throws Exception
    {
		
    	 List<Status> list = new ArrayList<>();
 		list.add(status);
 		when(statusServiceImpl.getAll()).thenReturn(list);
    	 
 		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/status/getAll");
		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult result = perform.andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status1 = response.getStatus();

		assertEquals(200, status1);
		assertNotNull(list);	   
    }
	
	@org.junit.jupiter.api.Test
    public void updateEmployeeRegisterStatus() throws Exception {
		StatusJson = null;
		StatusJson = mapper.writeValueAsString(status);
        when(statusServiceImpl.updateEmployeeRegisterStatus(1,"status")).thenReturn("status");

    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/service-catalog/status/employeeregister/update/1/active")
				.contentType(MediaType.APPLICATION_JSON).content(StatusJson);

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();

       assertEquals(200, status);
       
      
       

    }
	@org.junit.jupiter.api.Test
    public void getallEmployeeStatus()throws Exception
    {
		
    	 List<Status> list = new ArrayList<>();
 		list.add(status);
 		when(statusServiceImpl.getAll()).thenReturn(list);
    	 
 		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/status/employee/getAll");
		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult result = perform.andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status1 = response.getStatus();

		assertEquals(200, status1);
		assertNotNull(list);
	
	
    	   
    }

}

