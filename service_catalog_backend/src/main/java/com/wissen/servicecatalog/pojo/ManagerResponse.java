package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponse {

	 
		private String projectManagerName;
		private Integer employeeId;
		private Integer totalExperienceMonths;
		private Integer currentRoleExperienceMonths;
		private Integer managerId;
		private String primarySkill;
		private String secondarySkill;
		private String employeeStatus;
		private String currentEmployeeRoleName;
		private String applicationRoleName;
		private String emailId;
		private String certification;
		
}
