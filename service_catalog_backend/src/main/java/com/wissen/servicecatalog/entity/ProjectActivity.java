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
public class ProjectActivity {

	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer projectActivityId;
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project projectId;
    @ManyToOne
    @JoinColumn(name="activity_id")
	private Activity activityId;
    private String serviceApplicabilityYN;
    
}
