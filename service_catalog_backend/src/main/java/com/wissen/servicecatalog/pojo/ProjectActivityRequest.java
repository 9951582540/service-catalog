package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectActivityRequest {

	@NotNull(message = "Please Enter a valid name")
	private String projectname;
	@NotNull(message = "Please Enter the project manager id")
	private Integer projectManagerId;
	@NotNull(message = "Please Enter the activity name")
	private String activityname;
	@NotNull(message = "Please Enter the category")
	private String category;
	@NotNull(message = "Please Enter the service ")
	private String service;
	@NotNull(message = "Please Enter the facilitator ")
	private String facilitator;
	@NotNull(message = "Enter the technologies ")
	private String technologies;
	@NotNull(message = "Please Enter the skill required ")
	private String skill;
	@NotNull(message = "Please Enter the serviceapplicability yes or no ")
	private String serviceapplicabilityYN;

}
