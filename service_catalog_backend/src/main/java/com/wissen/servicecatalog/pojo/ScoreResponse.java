package com.wissen.servicecatalog.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreResponse
{
	private Integer scoreId;
	private Integer activityId;
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
	private String skill;
	private String feedbackName;
	private String projectSkill;
}
