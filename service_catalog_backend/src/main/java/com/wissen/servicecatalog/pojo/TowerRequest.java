package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TowerRequest {
	private Integer towerId;
	@NotNull(message = "Please enter the tower name")
	private String towerName;

}
