package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FeedbackRequest {
	
	
	
	
	private Integer feedbackId;
	@NotNull(message = "please enter the feedback")
	@NotEmpty(message = "please enter the feedback")
	private String feedbackName;
	

}
