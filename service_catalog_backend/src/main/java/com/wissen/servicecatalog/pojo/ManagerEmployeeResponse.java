package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerEmployeeResponse {

	private Integer employeeId;
	private String name;
	private Integer totalExperienceMonths;
	private Integer currentRoleExperienceMonths;
	private Integer managerId;
	private String primarySkill;
	private String secondarySkill;
	private String currentEmployeeRoleName;
	private String applicationRoleName;
	private String emailId;
}
