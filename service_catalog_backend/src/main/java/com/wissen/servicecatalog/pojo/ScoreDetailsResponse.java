package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDetailsResponse {

	private String category;
	private String service;
	private String technologies;
	private String activity;
	private String skill;
	private String facilitator;
	private Integer score;
	private String status;
}
