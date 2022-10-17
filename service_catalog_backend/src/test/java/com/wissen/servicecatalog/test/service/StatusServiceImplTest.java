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

import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.repository.StatusRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusServiceImplTest {
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@MockBean
	StatusRepository statusRepository;
	
	Status status=new Status(1,"active");
	Status status1=new Status(2,"active");

	
	@Test
	public void getAllStatus() throws Exception {
		List<Status> list = new ArrayList<Status>();
		list.add(status);
		list.add(status1);
		when(statusRepository.findAll()).thenReturn(list);
		
		assertNotNull(list);

		assertEquals(2, list.get(1).getStatusId());

	}
	@Test
	public void updateStatus() throws Exception {
		
		assertNotNull(status1);
		
		when(statusRepository.save(status1)).thenReturn(status1);
		
		assertEquals(2, status1.getStatusId());

	}

}
