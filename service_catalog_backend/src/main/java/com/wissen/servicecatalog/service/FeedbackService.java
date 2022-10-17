package com.wissen.servicecatalog.service;

import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.exception.FeedbackException;
import com.wissen.servicecatalog.pojo.FeedbackRequest;

public interface FeedbackService {

	public String addFeedback(FeedbackRequest feedback) throws FeedbackException;

	public Feedback updateFeedback(FeedbackRequest feedback, Integer feedbackId) throws FeedbackException;
}
