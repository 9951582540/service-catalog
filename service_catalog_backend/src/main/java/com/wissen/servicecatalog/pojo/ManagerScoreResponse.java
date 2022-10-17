package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerScoreResponse {
	private Integer employeeId;
	private String employeeName;
	private Integer scoreId;
	private String towerName;
	private String projectName;
	private String category;
	private String service;
	private String technologies;
	private String activityName;
	private String primarySkill;
	private String secondarySkill;
	private String facilitator;
	private Integer score;
	private String status;
	private String quarter;
	private Integer year;
	private String feedbackName;
	private String roadMap;
	private String timeLine;
	private String currentemployeestatus;
	private String projectrole;
	private Integer minimumScore;
	private Integer maximumScore;
	private Integer nextLevelMin;
}