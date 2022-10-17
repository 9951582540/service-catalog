package com.wissen.servicecatalog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Setting {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer settingId;
	@Column(unique = true)
	@NotNull(message = "Please enter the setting name")
	String name;
	@NotNull(message = "Please enter the setting description")
	String description;
	@NotNull(message="Please enter the data")
	String data;
	
	
}
