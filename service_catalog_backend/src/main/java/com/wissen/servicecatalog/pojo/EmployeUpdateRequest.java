package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeUpdateRequest 
{
	
	@NotNull(message = "Please specify the total month of experience")
	private Integer totalExperienceMonths;
	@NotNull(message = "Please specify the current role experience")
	private Integer currentRoleExperienceMonths;
	@NotEmpty(message="Please provide the Primary Skill")
	private String primarySkill;
	private boolean employeeStatus;
	@NotNull(message="Please enter the role")
	private String currentEmployeeRoleName;
	@NotEmpty(message="Please provide the Secondary Skill")
	private String secondarySkill;
	@NotEmpty(message="Please provide the Certification if not Mention as 'NA'")
	private String certification;

}
