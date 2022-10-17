package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	
	@NotNull(message = "Please enter the Employee Id")
	private String employeeId;

	@NotNull(message = "Please enter the password")
	private String password;
	
	

}
