package com.wissen.servicecatalog.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.TowerRequest;
import com.wissen.servicecatalog.pojo.TowerScoreRequest;
import com.wissen.servicecatalog.service.impl.TowerServiceImpl;

import io.swagger.annotations.Api;

@Api(tags = "Tower Service")
@RestController
@RequestMapping("/service-catalog/tower")
@CrossOrigin(origins = "*", maxAge = 3600) 
public class TowerController {
	Logger logger = LoggerFactory.getLogger(TowerController.class);
	@Autowired
	TowerServiceImpl towerService;

	@PostMapping("/add")
	public Tower createTower(@RequestBody @Valid TowerRequest tower) throws TowerException {
		logger.info("Adding Tower from TowerController");
		return towerService.createTower(tower);
	}

	
	@GetMapping("/getAll")
	public List<Tower> getAllTower() throws TowerException {
		logger.info("Getting all tower from Tower Controller");
		return towerService.getAllTower();
	}

	@PostMapping("/add/towerscore")
	public List<TowerScoreSkillDetails> addScore(@RequestBody List<TowerScoreRequest> towerRequest)
			throws TowerException {
		logger.info("Adding TowerScore from Tower Controller");
		return towerService.addTowerScore(towerRequest);
	}

	@GetMapping("/get/towerscore/{towerId}")
	public List<TowerScoreSkillDetails> getScore(@PathVariable(name = "towerId") Integer towerId)
			throws ScoreException {
		logger.info("Getting tower Score from Tower Id from Tower Controller");
		return towerService.getTowerScore(towerId);
	}

	@PutMapping("/update/{towerId}")
	public String updateTower(@RequestBody @Valid TowerRequest tower, @PathVariable Integer towerId) throws TowerException {
		logger.info("Update through tower Id from Tower Controller");
		return towerService.updateTower(tower, towerId);
	}

	@PutMapping("/update/score/{towerScoreDetailsId}/{minimumScore}/{maximumScore}/{nextLevelMini}")
	public ResponseEntity<TowerScoreSkillDetails> updateScore(@PathVariable Integer towerScoreDetailsId,
			@PathVariable Integer minimumScore, @PathVariable Integer maximumScore, Integer nextLevelMini)
			throws TowerException {
		logger.info("Update Score from Tower Controller");
		return towerService.updateTowerScore(towerScoreDetailsId, minimumScore, maximumScore, nextLevelMini);
	}

	@DeleteMapping("/delete-threshold")
	public String deleteThreshold(@RequestBody List<Integer> thresholdIds) throws TowerException {
		logger.info("Deleting threshold from Tower Controller");
		return towerService.deleteThreshold(thresholdIds);

	}
}