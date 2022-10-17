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
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.exception.SkillException;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.service.impl.SkillServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class SkillServiceImplTest {

	@MockBean
	SkillRepository skillRepository;

	@MockBean
	SkillServiceImpl skillServiceImpl;

	@MockBean
	AuthenticationManager authenticationManager;

	Skill skill_1 = new Skill(1, "Developer");
	Skill skill_2 = new Skill(2, "L1");

	@Test
	public void addSkill() throws SkillException {
		when(skillRepository.findBySkillId(0)).thenReturn(skill_1);

		when(skillRepository.save(skill_1)).thenReturn(skill_1);
		assertEquals("Developer", skill_1.getSkillLevel());

	}
	
	@Test
	public void getAllSkill() throws Exception {

		List<Skill> list = new ArrayList<Skill>();
		list.add(skill_1);
		list.add(skill_2);
		when(skillRepository.findAll()).thenReturn(list);

		assertNotNull(list);

		assertEquals("L1", list.get(1).getSkillLevel());

	}

	@Test
	public void getSkill() throws SkillException {

		when(skillRepository.findBySkillId(0)).thenReturn(skill_1);

		assertNotNull(skill_1);

		assertEquals("Developer", skill_1.getSkillLevel());
	}

	@Test
	public void updateSkill() throws SkillException {

		assertNotNull(skill_2);

		when(skillRepository.save(skill_2)).thenReturn(skill_2);

		assertEquals(2, skill_2.getSkillId());

	}

	

}
