package com.wissen.servicecatalog.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.pojo.ActivityRequest;
import com.wissen.servicecatalog.pojo.ActivityResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.service.impl.ActivityServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
 class ActivityControllerTest {

	@MockBean
	AuthenticationManager authenticationManager;

	@MockBean
	ActivityServiceImpl activityService;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	String activityResponseJson = null;

	@MockBean
	ActivityRepository activityRepository;

	@MockBean
	TowerRepository towerRepository;

	@MockBean
	SkillRepository skillRepository;

	ActivityResponse activityResponse1 = new ActivityResponse(1, "Managing service", "Backend", "developing", "SOP",
			"JAVA", "coding");

	ActivityRequest activityRequest1 = new ActivityRequest(18, 1, "Managing service", "Backend", "developing", "SOP",
			"JAVA", "coding");

	@MockBean
	Activity activity;

	Skill skill = new Skill(1, "L0");

	Tower tower = new Tower(1, "Develop");

	ActivityResponse activityResponse = new ActivityResponse(1, "Managing service", "Backend", "developing", "SOP",
			"JAVA", "coding");
//
	ActivityRequest activityRequest = new ActivityRequest(18, 1, "Managing service", "Backend", "developing", "SOP",
			"JAVA", "coding");

	@Test
	void getTowerId() throws Exception {

		List<ActivityResponse> activityResponseList = new ArrayList<>();

		activityResponseList.add(activityResponse);

		when(activityService.getTowerId(ArgumentMatchers.any())).thenReturn(activityResponseList);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/activity/get/1");

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

	}

//	@PutMapping("/update")
//	public List<ActivityResponse> updateActivity(@RequestBody @Valid List<ActivityRequest> activity) throws ActivityException, TowerException {
//		logger.info("Updating the Activity from Activity Controller");
//		return activityService.updateActivity(activity);
//	}
	@Test
	void updateActivity() throws Exception {
		List<ActivityRequest> list = new ArrayList<>();
		list.add(activityRequest);

		List<ActivityResponse> list1 = new ArrayList<>();
		list1.add(activityResponse);
		activityResponseJson = mapper.writeValueAsString(list);
		when(activityService.updateActivity(list)).thenReturn(list1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/service-catalog/activity/update")
				.contentType(MediaType.APPLICATION_JSON).content(activityResponseJson);

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);
	}

}
