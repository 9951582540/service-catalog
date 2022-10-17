package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.servicecatalog.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

	
	Skill findBySkillLevel(String skillLevel);

	Skill findBySkillId(Integer skillId);
}
