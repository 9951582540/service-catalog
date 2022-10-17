package com.wissen.servicecatalog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer projectId;
	private String projectName;
	@ManyToOne
	@JoinColumn(name = "project_manager_id", referencedColumnName = "employeeId")
	private EmployeeMaster projectManagerId;
	@ManyToOne
	@JoinColumn(name = "tower_id")
	private Tower tower;

}
