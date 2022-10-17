package com.wissen.servicecatalog.entity;


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
public class TowerScoreSkillDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer towerSkillDetailsId;
    @OneToOne
    @JoinColumn(name="skill_id")
    private Skill skill;
    @OneToOne
    @JoinColumn(name="tower_id")
    private Tower tower;
    private  Integer minimumScore;
    private Integer maximumScore;
    private Integer nextLevelMin;


}
