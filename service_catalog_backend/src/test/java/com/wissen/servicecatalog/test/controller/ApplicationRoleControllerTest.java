package com.wissen.servicecatalog.test.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
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

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.service.impl.ApplicationRoleServiceImpl;


@SpringBootTest
@AutoConfigureMockMvc
class ApplicationRoleControllerTest {

	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@Autowired
	private MockMvc mockMvc;
	

	
	
	
	ApplicationRoleMaster applicationRoleMaster = new ApplicationRoleMaster(1,"Employee");
	
	@MockBean
	ApplicationRoleServiceImpl applicationRoleServiceImpl;
	
	@Test
	  void getAllRoles() throws Exception
    {
		
		ArrayList<ApplicationRoleMaster> list = new ArrayList<ApplicationRoleMaster>();
		list.add(applicationRoleMaster);
		when(applicationRoleServiceImpl.getAllRoles()).thenReturn(list);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/applicationrole/getAll");
		
		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();
		assertEquals(200,status);
		
    }
	
	

}
