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

import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.repository.FeedbackRepository;
import com.wissen.servicecatalog.service.impl.FeedbackServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackServiceImplTest {

	@MockBean
	FeedbackRepository feedbackRepository;

	@MockBean
	FeedbackServiceImpl feedbackServiceImpl;

	@MockBean
	AuthenticationManager authenticationManager;

	Feedback feedback_1 = new Feedback(1, "Develop");
	Feedback feedback_2 = new Feedback(2, "Testing");


	@Test
	public void getAllFeedback() {
		ArrayList<Feedback> list = new ArrayList<Feedback>();
		list.add(feedback_1);
		list.add(feedback_2);
		when(feedbackRepository.findAll()).thenReturn(list);

		assertNotNull(list);

		assertEquals("Testing", list.get(1).getFeedbackName());

	}

	@Test
	public void updateFeedback() throws Exception {

		assertNotNull(feedback_2);

		when(feedbackRepository.save(feedback_2)).thenReturn(feedback_2);

		assertEquals(2, feedback_2.getFeedbackId());

	}

	@Test
	public void addFeedback() throws Exception {

		when(feedbackRepository.findByFeedbackId(feedback_1.getFeedbackId())).thenReturn(feedback_1);

	}

}
