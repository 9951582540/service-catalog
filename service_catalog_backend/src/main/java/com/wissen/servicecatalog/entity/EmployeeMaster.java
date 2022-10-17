package com.wissen.servicecatalog.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMaster implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(unique = true)
	private Integer employeeId;
	private String employeeName;
	private Integer totalExperienceMonths;
	private Integer currentRoleExperienceMonths;
	private Integer managerId;
	private String primarySkill;
	private String secondarySkill;
	private String certification;
	@Column(name = "email_id", unique = true)
	private String emailId;
	private String employeeStatus;

	@OneToOne
	@JoinColumn(name = "employee_role_id")
	private EmployeeRoleMaster currentEmployeeRoleId;
	@OneToOne
	@JoinColumn(name = "application_role_id")
	private ApplicationRoleMaster applicationRoleMaster;

}
