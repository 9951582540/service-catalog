package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SkillRequest {

	private Integer skillId;

	@NotNull(message = "Please select the skill level")
	@NotNull(message = "Please enter the skill level")
	private String skillLevel;
}
