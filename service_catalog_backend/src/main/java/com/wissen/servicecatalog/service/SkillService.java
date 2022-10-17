package com.wissen.servicecatalog.service;

import java.util.List;

import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.exception.SkillException;
import com.wissen.servicecatalog.pojo.SkillRequest;

public interface SkillService {

	public Skill addSkill(SkillRequest skill) throws SkillException;

	public Skill updateSkill(Integer skillId, String skillLevel) throws SkillException;

	public Skill getSkill(Integer skillId) throws SkillException;

	public List<Skill> getAllSkill() throws SkillException;

}
