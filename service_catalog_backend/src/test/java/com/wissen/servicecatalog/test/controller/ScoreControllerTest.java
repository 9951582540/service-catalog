package com.wissen.servicecatalog.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.pojo.PublishScore;
import com.wissen.servicecatalog.pojo.ScoreRequest;
import com.wissen.servicecatalog.pojo.ScoreResponse;
import com.wissen.servicecatalog.pojo.ScoreUpdateRequest;
import com.wissen.servicecatalog.service.ScoreService;

@SpringBootTest
@AutoConfigureMockMvc
 class ScoreControllerTest {

	@MockBean
	AuthenticationManager authenticationManager;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	Tower tower;

	@MockBean
	EmployeeMaster employeeMaster;

	@MockBean
	Project project;

	@MockBean
	Activity activity;

	@MockBean
	Feedback feedbackMaster;

	@MockBean
	Status currentEmployeeStatus;

	@MockBean
	Skill projectSkill;

//    @MockBean
//    ScoreServiceImpl scoreServiceImpl;

	@MockBean
	ScoreService scoreService;

	@Autowired
	ObjectMapper mapper;

	String scoreJson = null;

	ScoreResponse score1 = new ScoreResponse(2, 2, "Development", " Requirements Gathering", "Powerbi",
			"Data Model Requirements", "Java", "Spring", "Process", 2, "Occupied", "Q3", 2022, "Developer",
			"Over Qualified", "Java");

	ScoreRequest scoreRequest = new ScoreRequest(5872, 42, 18, 2, "Q3", 2022, 2, 2);

	ScoreUpdateRequest scoreUpdate = new ScoreUpdateRequest();

	Score score = new Score();

	@Test
	public void addScore() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<ScoreRequest> list = new LinkedList<>();
		list.add(scoreRequest);
		String scoreJson = mapper.writeValueAsString(list);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/service-catalog/score/add/Developer").contentType(MediaType.APPLICATION_JSON)
				.content(scoreJson);

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);
	}

	

	@Test
	public void getActivity() throws Exception {

		ArrayList<ScoreResponse> list = new ArrayList<>();
		list.add(score1);
		when(scoreService.getActivity(5872, 20, 43)).thenReturn(list);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/service-catalog/score/activity/get/5872/20/43");

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

		// assertEquals("Enable SSO", score1.getActivityName());
	}

	@Test
	public void getScore() throws Exception {

		ArrayList<ScoreResponse> list = new ArrayList<>();
		list.add(score1);
		when(scoreService.getScore(ArgumentMatchers.any())).thenReturn(list);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/score/get/5872");

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

		assertEquals(1, list.size());

	}

	

	@Test
	public void publishScore() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String scoreJson = null;
		ArrayList<PublishScore> a = new ArrayList<>();
		a.add(new PublishScore(1,2));
		scoreJson = mapper.writeValueAsString(a);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/service-catalog/score/publish")
				.contentType(MediaType.APPLICATION_JSON).content(scoreJson);

		when(scoreService.publishScore(a)).thenReturn("Updated");
		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();

		assertEquals(200, status);
	}

	@Test
	public void getScoreByTowerProjectQuarterYear() throws Exception {

		ArrayList<ScoreResponse> list = new ArrayList<>();
		list.add(score1);
		when(scoreService.getScoreByTowerProjectQuarterYear(5872, 1, 2, "Q1", 2022)).thenReturn(list);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/service-catalog/score/get/5872/1/2/Q1/2022");

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

		assertEquals(1, list.size());
	}

}
