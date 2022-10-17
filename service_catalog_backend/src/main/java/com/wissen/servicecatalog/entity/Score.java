package com.wissen.servicecatalog.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Score
{
	
	
	@Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer scoreId;
    private String quarter;
    private int year;
    private Integer score;
    private String roadMap;
    private LocalDateTime timeLine;

    private String status;
    @ManyToOne
    @JoinColumn(name = "tower_id")
    private Tower tower;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
    private EmployeeMaster employeeMaster;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @ManyToOne
    @JoinColumn(name = "feedBack_id")
    private Feedback feedbackMaster;
    @ManyToOne
    @JoinColumn(name="status_id")
    private Status currentEmployeeStatus;
    
    @ManyToOne
    @JoinColumn(name="skill_id")
    private Skill projectSkill;

}