package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VerifyOtp {
	
	@NotNull(message="Please enter the otp")
	private String otpNumber;
	
	
	
	
	

}
