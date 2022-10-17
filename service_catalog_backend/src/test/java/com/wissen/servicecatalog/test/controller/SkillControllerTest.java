package com.wissen.servicecatalog.test.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

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
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.service.SkillService;

@SpringBootTest
@AutoConfigureMockMvc
public class SkillControllerTest {

	@MockBean
	AuthenticationManager authenticationManager;

	@Autowired
	private MockMvc mockMvc;

	Skill skill = new Skill(1, "Developer");

	@MockBean
	SkillService skillService;

	@Autowired
	ObjectMapper mapper;

	String skillJson = null;

	@Test
	void addSkill() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		skillJson = mapper.writeValueAsString(skill);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service-catalog/skill/add")
				.contentType(MediaType.APPLICATION_JSON).content(skillJson);

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

		assertEquals(1, skill.getSkillId());
		assertEquals("Developer", skill.getSkillLevel());
	}

	@Test
	public void getAllSkill() throws Exception {

		ArrayList<Skill> list = new ArrayList<Skill>();
		list.add(skill);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/skill/getAll");

		when(skillService.getAllSkill()).thenReturn(list);

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();
		assertEquals(200, status);
		assertNotNull(list);
	}

	@Test
	public void getSkill() throws Exception {
		when(skillService.getSkill(ArgumentMatchers.any())).thenReturn(skill);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/skill/get/1");

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

		assertEquals("Developer", skill.getSkillLevel());

	}

	@Test
	public void updateSkill() throws Exception {
		skillJson = null;
		skillJson = mapper.writeValueAsString(skill);
		when(skillService.updateSkill(1, "Developer")).thenReturn(skill);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.patch("/service-catalog/skill/update/1/Developer").contentType(MediaType.APPLICATION_JSON)
				.content(skillJson);

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();

		assertEquals(200, status);
		assertEquals("Developer", skill.getSkillLevel());

	}

}
