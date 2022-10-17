package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectActivityResponse {

	private Integer projectActivityId;
	private String projectname;
	private Integer projectManagerId;
	private String activityname;
	private String category;
	private String service;
	private String facilitator;
	private String technologies;
	private String skill;
	private String serviceapplicabilityYN;
}
