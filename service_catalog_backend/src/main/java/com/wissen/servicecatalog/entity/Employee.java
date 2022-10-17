package com.wissen.servicecatalog.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_register")

public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	@Column(name = "Id")
	private Integer id;

	@Column(name = "Employee_Id")

	private String employeeId;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;
	private LocalDateTime localDateTime;

	@ManyToOne
	@JoinColumn(name = "application_role_id")
	private ApplicationRoleMaster applicationRole;

	private String status;
	@JsonIgnore
	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	
}