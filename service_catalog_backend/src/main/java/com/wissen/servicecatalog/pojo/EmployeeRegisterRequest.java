package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern.Flag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRegisterRequest {
	
	@NotEmpty(message="Please enter the employee id")
	private String employeeId;
	@Email(message = "Please enter the proper email")
	@NotNull(message="Please enter the email")
	@Email(regexp = "^[A-Za-z0-9._%+-]+@wisseninfotech.com",flags=Flag.CASE_INSENSITIVE,message = "Please enter the wissen mail id")
    private String email;
	@NotNull(message="Please enter the password")
    private String password;
	@NotNull(message="Please select the role")
    private String applicationRole;

}
