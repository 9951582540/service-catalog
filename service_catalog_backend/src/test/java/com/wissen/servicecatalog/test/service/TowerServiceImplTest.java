package com.wissen.servicecatalog.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.service.impl.TowerServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class TowerServiceImplTest {

	@MockBean
	TowerRepository towerRepository;

	@MockBean
	TowerServiceImpl towerServiceImpl;

	@MockBean
	AuthenticationManager authenticationManager;

	Tower TOWER_1 = new Tower(1, "Develop");
	Tower TOWER_2 = new Tower(2, "Testing");

	@Test
	public void createTower() throws Exception {
		when(towerRepository.save(TOWER_1)).thenReturn(TOWER_1);

		assertNotNull(TOWER_1);

		assertEquals(1, TOWER_1.getTowerId());

	}

	@Test
	public void getAllTower() {
		ArrayList<Tower> list = new ArrayList<Tower>();
		list.add(TOWER_1);
		list.add(TOWER_2);
		when(towerRepository.findAll()).thenReturn(list);

		assertNotNull(list);

		assertEquals("Testing", list.get(1).getTowerName());

	}

	@Test
	public void getTower() {

		when(towerRepository.findByTowerId(1)).thenReturn(TOWER_1);

		assertNotNull(TOWER_1);

		assertEquals("Develop", TOWER_1.getTowerName());

	}

	@Test
	public void updateTower() throws Exception {

		assertNotNull(TOWER_2);

		when(towerRepository.save(TOWER_2)).thenReturn(TOWER_2);

		assertEquals(2, TOWER_2.getTowerId());

	}

}
