package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest
{
	@NotNull(message="Please enter the tower id")
	private Integer towerId;
	@NotNull(message="Please enter the activity id")
	private Integer activityId;
	@NotBlank(message="Please enter the activity")
	@NotNull(message = "Please enter the activity ")
    private String activityName;
	@NotBlank(message="Please enter the category")
	@NotNull(message = "Please enter the category")
    private String category;
	@NotBlank(message="Please enter the service")
	@NotNull(message = "Please enter the service")
    private String service;
	@NotBlank(message="Please enter the facilitator")
	@NotNull(message = "Please enter the facilitator")
    private String facilitator;
	@NotBlank(message="Please enter the technologies")
	@NotNull(message = "Please enter the technologies")
    private String technologies;
	@NotBlank(message="Please enter the skill")
	@NotNull(message = "Please enter the skill level")
    private String skillLevel;
    
}
