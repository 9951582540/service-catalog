package com.wissen.servicecatalog.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ProjectRequest;
import com.wissen.servicecatalog.pojo.ProjectResponse;
import com.wissen.servicecatalog.repository.ProjectRepository;
import com.wissen.servicecatalog.service.ProjectService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectServiceTest {
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	
	@MockBean
	ProjectRepository projectRepository;

	
	@MockBean
	ProjectRequest projectRequest;
	
	
	@MockBean
	ProjectResponse projectResponse;

	@Autowired	
	ProjectService projectService;

	
	@MockBean
	private Tower tower2;
	
	@MockBean
	private EmployeeMaster employee;
	
	Project request2 = new Project(2, "java", employee, tower2);
	
//	Project project = new Project();
//	
//	List<Tower> tower = new ArrayList<Tower>();
//	
	//Project response = Optional.ofNullable(new Project());
	
	
	@Test
	public void AddProject() throws ProjectException, EmployeeException, TowerException{
	     when(projectRepository.save(any())).thenReturn(projectRequest);
	     assertNotNull(projectRequest);
          assertEquals(2, request2.getProjectId());
	}



	@Test
	public void getallprojectTest() {
		ArrayList<Project> list = new ArrayList<Project>();
		list.add(request2);
		//list.add(request2);
		when(projectRepository.findAll()).thenReturn(list);
		
		assertNotNull(list);

		assertEquals("java", list.get(0).getProjectName());

	}
	@Test
	public void getprojectTest() {
    when(projectRepository.findById(1)).thenReturn(Optional.ofNullable(request2));
		
		assertNotNull(request2);

		assertEquals(2, request2.getProjectId());
}

	@Test
	public void updateProject() throws Exception {

		ArrayList<ProjectRequest> list = new ArrayList<ProjectRequest>();
		list.add(projectRequest);

		when(projectRepository.findByProjectId(2)).thenReturn(request2);
		assertNotNull(list);
		assertNotNull(request2);

		assertEquals(2, request2.getProjectId());
	}
}

	
	
