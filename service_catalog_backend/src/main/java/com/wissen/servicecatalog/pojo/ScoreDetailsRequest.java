package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDetailsRequest {

	@NotEmpty(message = "Please enter the quater")
	private String quater;
	@NotNull(message = "please Enter the valid year")
	private int year;
	@NotNull(message = "please enter the score")
	private int score;
	@NotNull(message = "Please enter the employee Id")
	private Integer employeeId;
	@NotEmpty(message = "please enter the project Name")
	private String projectName;
	@NotNull(message = "please enter the activity id")
	private Integer activityId;
	@NotEmpty(message = "please enter the tower Name")
	private String towerName;
	@NotEmpty(message = "Please enter the feedBack")
	private String feedBackName;

}
