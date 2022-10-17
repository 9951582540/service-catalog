package com.wissen.servicecatalog.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.pojo.ScoreRequest;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.ScoreRepository;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.service.ProjectService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectManagerServiceTest {
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@MockBean
	SettingRepository settingRepository;

	@MockBean
	EmployeeMasterRepository employeeMasterRepository;


	@MockBean
	ScoreRepository scoreRepository;

	
	@Autowired
	ProjectService projectService;
	
	@MockBean
	private Tower tower2;
	
	@MockBean
	private Score score;
	
	@MockBean
	private EmployeeMaster employee;

	
	Score scoreRequest_2 = new Score();

	
	@MockBean
	private Project project;
	
	@MockBean
	private Activity activity;
	
	@MockBean
	private Skill skill;
	
	@MockBean
	private Status status;
	
	@MockBean
	private Feedback feedback;
	
	@MockBean
	ScoreRequest ScoreRequest;
	
	List<Score> score3 = new LinkedList<Score>();
	
	Project request = new Project(1, "abc", employee, tower2);
	Project request2 = new Project(2, "mpr", employee, tower2);
	
	Tower tower = new Tower(1,"Service Catalog");
	Optional<Score> score1 = Optional.of(new  Score( 1,"Q3",5,6,"Nothing",LocalDateTime.now(), "abc", tower2, employee, request, activity, feedback, status, skill));
	
	Optional<Score> score2 = Optional.of(new  Score( 1,"Q3",5,6,"Nothing",LocalDateTime.now(), "abc", tower2, employee, request, activity, feedback, status, skill));
	//Optional<Score> score3 = Optional.of(new Score());
	
	
	
	@Test
	public void getprojectbyManagerId() {
		ArrayList<Project> list = new ArrayList<Project>();
		list.add(request);
		list.add(request2);
		when(scoreRepository.findById(employee.getManagerId())).thenReturn(score2);
		
		assertNotNull(list);

		assertEquals("mpr", list.get(1).getProjectName());

	}

		@Test
		public void updateScore() throws Exception {

			ArrayList<ScoreRequest> list = new ArrayList<ScoreRequest>();
			list.add(ScoreRequest);

			when(scoreRepository.findByScoreId(2)).thenReturn(scoreRequest_2);
			assertNotNull(list);
			assertNotNull(scoreRequest_2);

			assertEquals(0, list.get(0).getScoreId());
		}
	}