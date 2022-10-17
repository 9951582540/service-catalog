package com.wissen.servicecatalog.test.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.pojo.TowerRequest;
import com.wissen.servicecatalog.service.impl.TowerServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class TowerControllerTest {

	@MockBean
	AuthenticationManager authenticationManager;

	@MockBean
	TowerServiceImpl towerService;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;
    
	String towerJson = null;
	
    
	
	TowerRequest TOWER_1 = new TowerRequest(1, "Develop"); 
	
	Tower TOWER_2 = new Tower(1, "Develop");

	@Test
	public void creatTower() throws Exception {
		 ObjectMapper mapper = new ObjectMapper();
		 towerJson = mapper.writeValueAsString(TOWER_1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service-catalog/tower/add")
				.contentType(MediaType.APPLICATION_JSON).content(towerJson);
		
		ResultActions perform = mockMvc.perform(requestBuilder);
		
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(1, TOWER_1.getTowerId());
		assertEquals("Develop", TOWER_1.getTowerName());

		assertEquals(200, status);

	}
	

	
	@Test
	public void getAll() throws Exception {
		
		ArrayList<Tower> list = new ArrayList<Tower>();
		list.add(TOWER_2);
		when(towerService.getAllTower()).thenReturn(list);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/tower/getAll");
		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult result = perform.andReturn();
		MockHttpServletResponse response = result.getResponse();
		int status = response.getStatus();

		assertEquals(200, status);
		assertNotNull(list);

	}

	@Test
	public void updateTower() throws Exception {
		towerJson = null;
		towerJson = mapper.writeValueAsString(TOWER_1);
		when(towerService.updateTower(TOWER_1, 1)).thenReturn("Updated tower");

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/service-catalog/tower/update/1")
				.contentType(MediaType.APPLICATION_JSON).content(towerJson);

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();

		assertEquals(200, status);
		assertEquals("Develop", TOWER_1.getTowerName());

	}
		@Test
	    public void deleteThreshould() throws Exception { 
			
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(1);
			towerJson = null;
			towerJson = mapper.writeValueAsString(list);
			mockMvc.perform(MockMvcRequestBuilders.delete("/service-catalog/tower/delete-threshold")
			.contentType(MediaType.APPLICATION_JSON).content(towerJson));
	        Mockito.when(towerService.deleteThreshold(list)).thenReturn("SUCCESS");
	    }
		@Test
		public void updateScore() throws Exception {
			towerJson = null;
			towerJson = mapper.writeValueAsString(TOWER_1);
			when(towerService.updateTowerScore(1, 1, 1, 1)).thenReturn(null);

			MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/service-catalog/tower/update/score/1/1/1/1")
					.contentType(MediaType.APPLICATION_JSON).content(towerJson);

			ResultActions perform = mockMvc.perform(requestBuilder);
			MvcResult mvcResult = perform.andReturn();

			MockHttpServletResponse response = mvcResult.getResponse();
			int status = response.getStatus();

			assertEquals(200, status);
			assertEquals("Develop", TOWER_1.getTowerName());

		}
		@Test
		public void getScorer() throws Exception {
			ArrayList<TowerScoreSkillDetails> list = new ArrayList<TowerScoreSkillDetails>();
			towerJson = null;
			towerJson = mapper.writeValueAsString(list);
			
			when(towerService.getTowerScore(ArgumentMatchers.any())).thenReturn(list);

			MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/tower/get/towerscore/1");

			ResultActions perform = mockMvc.perform(requestBuilder);

			MvcResult mvcResult = perform.andReturn();

			MockHttpServletResponse response = mvcResult.getResponse();

			int status = response.getStatus();

			assertEquals(200, status);

			

		}
		  @Test
		    public void addScore() throws Exception {
			  TowerScoreSkillDetails towerScoreSkillDetails=new TowerScoreSkillDetails();
				List<TowerScoreSkillDetails> list = new ArrayList<TowerScoreSkillDetails>();
				
				list.add(towerScoreSkillDetails);
				towerJson = null;
				towerJson = mapper.writeValueAsString(list);
				mockMvc.perform(MockMvcRequestBuilders.post("/service-catalog/activity/add/towerscore")
				.contentType(MediaType.APPLICATION_JSON).content(towerJson));
		        Mockito.when(towerService.getTowerScore(1)).thenReturn(list);
			}
}
