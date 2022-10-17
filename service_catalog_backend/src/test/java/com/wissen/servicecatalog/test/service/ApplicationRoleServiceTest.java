package com.wissen.servicecatalog.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationRoleServiceTest {
	
	
	@MockBean
	AuthenticationManager authenticationManager;

	@MockBean
	ApplicationRoleMasterRepository applicationRoleMasterRepository;
	
	
	@Test
	public void getAllRoles() throws EmployeeException
    { 
		ArrayList<ApplicationRoleMaster> list= new ArrayList<>(Arrays.asList(new ApplicationRoleMaster(1,"Employee")));
		
		when(applicationRoleMasterRepository.findAll()).thenReturn
		(new ArrayList<ApplicationRoleMaster>(Arrays.asList(new ApplicationRoleMaster(1,"Employee"))));
		if(list.size()==0)
			
		assertEquals(list.get(0).getApplicationName(), "Employee");
	
				
    }
	

}
