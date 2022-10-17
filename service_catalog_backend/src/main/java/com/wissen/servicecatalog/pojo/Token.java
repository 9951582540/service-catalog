package com.wissen.servicecatalog.pojo;

import lombok.Data;

@Data
public class Token {
	
	private String firstName;
	private String email;
	private String userName;
	
	private String jwtToken;
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	

	
	

}
