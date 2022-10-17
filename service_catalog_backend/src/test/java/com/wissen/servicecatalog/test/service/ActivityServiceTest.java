package com.wissen.servicecatalog.test.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.pojo.ActivityRequest;
import com.wissen.servicecatalog.pojo.ActivityResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.TowerRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ActivityServiceTest {

	@MockBean
	AuthenticationManager authenticationManager;

//	@MockBean
//	ActivityService activityService;

//	@InjectMocks
//	ActivityServiceImpl activityServiceImpl;
	

	Tower tower = new Tower(1, "Develop");

	@MockBean
	ActivityRepository activityRepository;

	@MockBean
	TowerRepository towerRepository;

	@MockBean
	SkillRepository skillRepository;
	
	ActivityResponse activityResponse =null;
	
	
	ActivityRequest activityRequest =null;
	
	Skill skill = null;
	
	 @BeforeEach
	    void setUp() {
	 activityResponse = new ActivityResponse(1, "Managing service", "Backend", "developing", "SOP",
			"JAVA", "coding");

	 activityRequest = new ActivityRequest(1,null, "Managing service", "Backend", "developing", "SOP", "JAVA",
			"coding");
	 
	 skill = new Skill(1, "L0");

	 }
	@MockBean
	Activity activity;

	
	@Test
	public void addActivity_success() throws Exception {

		ModelMapper modelMapper = new ModelMapper();

		when(skillRepository.findBySkillLevel(activityRequest.getSkillLevel())).thenReturn(skill);

		Activity activity = modelMapper.map(activityRequest, Activity.class);
		Skill newLevelSkill = new Skill();
		newLevelSkill.setSkillLevel(activityRequest.getSkillLevel());
		skillRepository.save(newLevelSkill);
		activity.setSkill(newLevelSkill);
		activity.setTower(tower);
		
		when(activityRepository.save(activity)).thenReturn(activity);

		ActivityResponse response = modelMapper.map(activity, ActivityResponse.class);

		response.setSkill(activityRequest.getSkillLevel());

		assertNotNull(response);

	}

	

	@Test
	public void getTowerId() {

		ModelMapper modelMapper = new ModelMapper();

		when(towerRepository.findByTowerId(1)).thenReturn(tower);

		List<Activity> activityList = new ArrayList<Activity>();
		activityList.add(activity);

		when(activityRepository.getActivity(tower.getTowerId())).thenReturn(activityList);

		List<ActivityResponse> activityResponseList = new ArrayList<ActivityResponse>();
		for (Activity activity : activityList) {
			ActivityResponse activityResponse = modelMapper.map(activity, ActivityResponse.class);
			activityResponse.setSkill("L0");
			activityResponseList.add(activityResponse);

			assertNotNull(activityResponseList);

}
	}

	@Test
	public void updateActivity_success(){

		ModelMapper modelMapper = new ModelMapper();

		when(skillRepository.findBySkillLevel(activityRequest.getSkillLevel())).thenReturn(skill);

		when(activityRepository.findByActivityId(1)).thenReturn(activity);

		modelMapper.map(activityRequest, activity);
		Skill newLevelSkill = new Skill();
		newLevelSkill.setSkillLevel(activityRequest.getSkillLevel());
		skillRepository.save(newLevelSkill);
		activity.setActivityId(1);
		activity.setSkill(newLevelSkill);

		when(activityRepository.save(activity)).thenReturn(activity);

		ActivityResponse response = modelMapper.map(activity, ActivityResponse.class);
		response.setActivityId(1);
		response.setSkill("L0");

		assertNotNull(response);


	}

	//@Test
//	public void updateActivity_failure() {
//
////		ModelMapper modelMapper = new ModelMapper();
//
//		when(skillRepository.findBySkillLevel(activityRequest.getSkillLevel())).thenReturn(skill);

//		modelMapper.map(activityRequest, activity);
//		activity.setSkill(skill);
//		activity.setActivityId(1l);

		//when(activityRepository.save(activity)).thenReturn(activity);

//		ActivityResponse response = modelMapper.map(activity, ActivityResponse.class);
//
//		response.setSkill("L0");
//
//		assertNotNull(response);
//
//		assertEquals(response, response);

	//}

}
