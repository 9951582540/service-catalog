package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class SettingRequest {

	
	
	Integer settingId;
	@NotNull(message = "Please enter the setting name")
	String name;
	@NotNull(message = "Please enter the setting description")
	String description;
	@NotNull(message="Please enter the data")
	String data;
	
}
