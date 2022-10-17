package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {


	
	@NotNull(message="please enter the employee id")
	private String employeeId;
	@NotNull(message="please enter the role name")
	private String applicationRole;
	@NotNull(message="please enter the employee status")
	private String employeeStatus;
	
	
}
