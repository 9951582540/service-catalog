package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDetails {
	Integer employeeId;
	String employeeName;
	String towerName;
	String skillLevel;
	String projectName;
	String primarySkill;
	String secondarySkill;
	Integer previousQuarter;
	Integer currentQuarter;
	String quarterComparsion;
	String feedbackName;
	Integer minScore;
	Integer maxScore;
	String employeeStatus;
	String projectManagerName;
	String roadMap;
}
