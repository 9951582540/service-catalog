package com.wissen.servicecatalog.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
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
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.pojo.ScoreRequest;
import com.wissen.servicecatalog.pojo.ScoreResponse;
import com.wissen.servicecatalog.repository.ScoreRepository;
import com.wissen.servicecatalog.service.impl.ScoreServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class ScoreServiceImplTest {
	@MockBean
	AuthenticationManager authenticationManager;

	@MockBean
	Tower tower;

	@MockBean
	EmployeeMaster employeeMaster;

	@MockBean
	Project project;

	@MockBean
	Activity activity;

	@MockBean
	Status currentEmployeeStatus;

	@MockBean
	Feedback feedbackMaster;

	@MockBean
	Skill skill;

	Score score_1 = new Score();
	Score score_2 = new Score();

	@MockBean
	ScoreRepository scoreRepository;

	@MockBean
	ScoreServiceImpl scoreServiceImpl;

	@MockBean
	ScoreRequest ScoreRequest;

	@MockBean
	ScoreResponse scoreResponse;

	ScoreResponse scoreResponse_1 = new ScoreResponse(2, 2, "Development", " Requirements Gathering", "Powerbi",
			"Data Model Requirements", "Java", "Spring", "Process", 2, "Occupied", "Q3", 2022, "Developer",
			"Over Qualified", "Java");

	ScoreRequest scoreRequest = new ScoreRequest(5872, 42, 18, 2, "Q3", 2022, 2, 2);
	Score scoreRequest_2 = new Score();

	@Test
	public void addScore() throws ScoreException {
		when(scoreRepository.findByScoreId(scoreResponse.getScoreId())).thenReturn(score_1);
		when(scoreRepository.save(score_1)).thenReturn(score_1);
	}

	@Test
	public void saveScore() throws ScoreException {

		when(scoreRepository.save(score_2)).thenReturn(score_2);
	}

	@Test
	public void getScore() throws ScoreException {
		when(scoreRepository.findByScoreId(1)).thenReturn(score_1);
		assertNotNull(score_1);
	}

	@Test
	public void getScoreByTowerAndProject() throws ScoreException {
		ArrayList<Score> list = new ArrayList<Score>();
		list.add(score_1);
		list.add(score_2);
		when(scoreRepository.findByTowerNameAndProjectAndEmployeeId("Database", "Service Catalog", 5872))
				.thenReturn(list);

		assertNotNull(list);
	}

	@Test
	public void getActivity() throws Exception {
		ArrayList<ScoreResponse> list = new ArrayList<ScoreResponse>();
		list.add(scoreResponse_1);

		when(scoreRepository.findByProjectId(2)).thenReturn(scoreRequest_2);

		assertNotNull(scoreResponse);

		assertEquals(0, scoreResponse.getActivityId());

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

	@Test
	public void publishScore() throws ScoreException {
		ArrayList<Score> list = new ArrayList<Score>();
		list.add(score_1);
		list.add(score_2);
		when(scoreRepository.saveAll(list)).thenReturn(list);
		assertNotNull(score_1);
	}

	@Test
	public void getScoreByTowerProjectQuarterYear() throws ScoreException {
		ArrayList<Score> list = new ArrayList<Score>();
		list.add(score_1);
		list.add(score_2);
		when(scoreRepository.findByPublisedScore(5872, 2, 2, "Q1", 2022, "Published")).thenReturn(list);
		assertNotNull(list);
	}

	@Test
	public void getScoresByTowerProjectQuarterYear() throws ScoreException {
		ArrayList<Score> list = new ArrayList<Score>();
		list.add(score_1);
		list.add(score_2);
		when(scoreRepository.findByPublisedScore(5872, 2, 2, "Q1", 2022, "Published")).thenReturn(list);
		assertNotNull(list);
	}

}
