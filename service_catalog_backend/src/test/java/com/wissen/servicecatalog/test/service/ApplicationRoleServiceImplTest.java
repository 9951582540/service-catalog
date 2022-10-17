package com.wissen.servicecatalog.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationRoleServiceImplTest {
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@MockBean
	ApplicationRoleMasterRepository applicationRoleMasterRepository;

	
	ApplicationRoleMaster applicationRoleMaster1=new ApplicationRoleMaster(1,"employee");
	ApplicationRoleMaster applicationRoleMaster2=new ApplicationRoleMaster(2,"employee");

	@Test
	public void getAllRoles() throws Exception {
		List<ApplicationRoleMaster> list = new ArrayList<ApplicationRoleMaster>();
		list.add(applicationRoleMaster1);
		list.add(applicationRoleMaster2);
		when(applicationRoleMasterRepository.findAll()).thenReturn(list);
		
		assertNotNull(list);

		assertEquals(2, list.get(1).getApplicationRoleId());

	}



}
