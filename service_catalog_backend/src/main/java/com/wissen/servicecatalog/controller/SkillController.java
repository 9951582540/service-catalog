package com.wissen.servicecatalog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.pojo.SkillRequest;
import com.wissen.servicecatalog.service.SkillService;

import io.swagger.annotations.Api;

@Api(tags = "Skill Service")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600) 
@RequestMapping("/service-catalog/skill")
public class SkillController {

	Logger logger = LoggerFactory.getLogger(SkillController.class);

	@Autowired
	SkillService skillService;

	@PostMapping("/add")
	public Skill addSkill(SkillRequest skill) {
		logger.info("Adding skill from Skill Controller");
		return skillService.addSkill(skill);
	}

	@GetMapping("/get/{skillId}")
	public Skill getSkill(@PathVariable Integer skillId) {
		logger.info("Getting skill by skill Id from Skill Controller");
		return skillService.getSkill(skillId);
	}

	@PatchMapping("/update/{skillId}/{skillLevel}")
	public Skill updateSkill(@PathVariable Integer skillId, @PathVariable String skillLevel) {
		logger.info("Updating skill by skill Id and skill Level from Skill Controller");
		return skillService.updateSkill(skillId, skillLevel);
	}

	@GetMapping("/getAll")
	public List<Skill> getAllSkill() {
		logger.info("Getting all skills from Skill Controller");
		return skillService.getAllSkill();
	}

}
