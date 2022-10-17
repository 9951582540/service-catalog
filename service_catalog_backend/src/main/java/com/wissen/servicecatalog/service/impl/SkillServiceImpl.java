package com.wissen.servicecatalog.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.exception.SkillException;
import com.wissen.servicecatalog.pojo.SkillRequest;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {
	Logger logger = LoggerFactory.getLogger(SkillServiceImpl.class);
	@Autowired
	SkillRepository skillRepository;

	@Override
	public Skill addSkill(SkillRequest skill) throws SkillException {
		Skill level = skillRepository.findBySkillLevel(skill.getSkillLevel());
		if (level == null) {
			Skill levelSave= new Skill();
			levelSave.setSkillLevel(skill.getSkillLevel());
			skillRepository.save(levelSave);
		} else {
			throw new SkillException("Skill is already exist!");
		}
		return level;
	}

	@Override
	public Skill updateSkill(Integer skillId, String skillLevel) throws SkillException {
		logger.info("Updating skill from Skill service");
		if (skillId == null) {
			logger.error("Please enter the skill id");
			throw new SkillException("Please enter the skill id");
		}

		Skill skill = skillRepository.findBySkillId(skillId);
		if (skill == null) {
			logger.error("Invalid Skill Id");
			throw new SkillException("Invalid Skill Id");
		}
		skill.setSkillLevel(skillLevel);

		skillRepository.save(skill);

		return skill;
	}

	@Override
	public Skill getSkill(Integer skillId) throws SkillException {
		logger.info("Getting skill from skill Service");
		if (skillId == null) {
			logger.error("Please enter the skill id");
			throw new SkillException("Please enter the skill id");
		}
		Skill skill = skillRepository.findBySkillId(skillId);
		if (skill == null) {
			logger.error("No skills found");
			throw new SkillException("No skills found");
		}
		return skill;
	}

	@Override
	public List<Skill> getAllSkill() throws SkillException {
		logger.info("Getting all skills from Skill service");
		List<Skill> skill = skillRepository.findAll();
		if (skill.isEmpty()) {
			logger.error("No skills found");
			throw new SkillException("No skills found");
		}
		return skill;
	}

}
