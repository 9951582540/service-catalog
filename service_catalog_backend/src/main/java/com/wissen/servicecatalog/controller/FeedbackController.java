package com.wissen.servicecatalog.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.exception.FeedbackException;
import com.wissen.servicecatalog.pojo.FeedbackRequest;
import com.wissen.servicecatalog.service.impl.FeedbackServiceImpl;

import io.swagger.annotations.Api;

@Api(tags = "Feedback Service")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/service-catalog/feedback")
public class FeedbackController {

	Logger logger = LoggerFactory.getLogger(FeedbackController.class);

	@Autowired
	FeedbackServiceImpl feedbackService;

	@PostMapping("/add")
	public String addFeedback(@RequestBody @Valid FeedbackRequest feedback) throws FeedbackException {
		logger.info("Adding the feedback from Feedback Controller");
		return feedbackService.addFeedback(feedback);
	}

	@PutMapping("/update/{feedbackId}")
	public Feedback updateFeedback(@RequestBody @Valid FeedbackRequest feedback, @PathVariable Integer feedbackId)
			throws FeedbackException {
		logger.info("Update the feedback from Feedback Controller");
		return feedbackService.updateFeedback(feedback, feedbackId);
	}

	@GetMapping("/getAll")
	public List<Feedback> getAllFeedback() throws FeedbackException {
		logger.info("Getting all Feddback from the Controller");
		return feedbackService.getAllFeedback();
	}

}
