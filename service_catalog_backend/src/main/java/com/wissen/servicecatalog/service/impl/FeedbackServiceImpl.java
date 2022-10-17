package com.wissen.servicecatalog.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.exception.FeedbackException;
import com.wissen.servicecatalog.pojo.FeedbackRequest;
import com.wissen.servicecatalog.repository.FeedbackRepository;
import com.wissen.servicecatalog.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);
	@Autowired
	FeedbackRepository feedbackRepository;

	@Override
	public String addFeedback(FeedbackRequest feedback) throws FeedbackException {
		logger.info("Adding Feedback");
		Feedback findByFeedbackName = feedbackRepository.findByFeedbackName(feedback.getFeedbackName());
		
		if (findByFeedbackName !=null) {
			logger.error("Feedback name already exists");
			throw new FeedbackException("Feedback name already exists");
		
		}
		Feedback feedbackSave= new Feedback();
		feedbackSave.setFeedbackName(feedback.getFeedbackName());
		
		feedbackRepository.save(feedbackSave);
		return "Feedback added";
		
	}

	

	@Override
	public Feedback updateFeedback(FeedbackRequest feedback, Integer feedbackId) throws FeedbackException {
		logger.info("Updating Feedback from Feedback Service");
		if (feedbackId != null) {
			Feedback feedbackUpdate = feedbackRepository.findByFeedbackId(feedbackId);
			if (feedbackUpdate != null) {
				feedbackUpdate.setFeedbackName(feedback.getFeedbackName());
				return feedbackRepository.save(feedbackUpdate);
			}
			
			logger.error("Invalid Feedback id");
			throw new FeedbackException("Invalid feedback ID");

		}
		logger.error("Please enter the feedback ID");
		throw new FeedbackException("Please enter the feedback ID");
	}

	public List<Feedback> getAllFeedback() throws FeedbackException {
		List<Feedback> list = feedbackRepository.findAll();
		if (list.isEmpty()) {
			logger.error("No feedback found");
			throw new FeedbackException("No feedback found");
		}
		return list;
	}

}