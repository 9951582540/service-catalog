package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRequest {
	
	@NotNull(message="Please enter the employee id")
	private Integer employeeId;
	@NotNull(message="Please enter the project id")
	private Integer projectId;
	@NotNull(message="Please enter the project id")
	private Integer towerId;
	@NotNull(message="Please enter the activity id")
	private Integer activityId;
	@NotNull(message="Please enter the quarter")
	private String quarter;
	@NotNull(message="Please enter the year")
	private Integer year;
	@NotNull(message="Please enter the score")
	private Integer score;
	private Integer scoreId;
	
	
}