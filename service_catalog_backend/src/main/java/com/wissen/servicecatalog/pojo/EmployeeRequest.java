package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern.Flag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
	private Integer id;
	@NotNull(message = "Please enter the employee ID")
	private Integer employeeId;
	@NotNull(message = "Please enter the name")
	private String employeeName;
	private Integer totalExperienceMonths;
	@NotNull(message="Please enter the current role experience")
	private Integer currentRoleExperienceMonths;
	@NotNull(message = "Please enter the manager id")
	private Integer managerId;
	private String primarySkill;
	private String secondarySkill;
	private String currentEmployeeRoleName;
	@NotNull(message = "Please enter the application role ")
	private String applicationRoleName;
	@NotNull(message = "Please enter the email")
	@Email(message = "Please enter the wissen mail id", regexp = "^[A-Za-z0-9._%+-]+@wisseninfotech.com", flags = Flag.CASE_INSENSITIVE)
	private String emailId;
	private String certification;

}
