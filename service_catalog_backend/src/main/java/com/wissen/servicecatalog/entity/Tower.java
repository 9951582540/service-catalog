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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tower {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tower_id")
	private Integer towerId;
	@NotNull(message = "Please enter the tower name")
	@Column(name = "tower_name")
	private String towerName;
}
