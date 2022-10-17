package com.wissen.servicecatalog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity
{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer activityId;
    @Column(name="activity_name",length = 1000)
    private String activityName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="towerId")
    private Tower tower;
    private String category;
    private String service;
    private String facilitator;
    private String technologies;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="skillId")
    private Skill skill;
    
}
