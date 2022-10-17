package com.wissen.servicecatalog.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.TowerRequest;
import com.wissen.servicecatalog.pojo.TowerScoreRequest;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.repository.TowerSkillScoreRepository;
import com.wissen.servicecatalog.service.TowerService;

@Service
public class TowerServiceImpl implements TowerService {
	private static final String NO_SCORE_DETAILS_IN_THIS_TOWER = "No score details in this Tower";
	private static final String TOWER_DOESN_T_EXIST = "Tower doesn't exist ";
	private static final String SKILL_DOESN_T_EXIST = "Skill doesn't exist ";
	private static final String PLEASE_ENTER_TOWER_ID = "Please enter Tower Id";
	private static final String TOWER_WITH_SAME_NAME_ALREADY_EXISTS = "Tower already exists";
	private static final String NO_TOWERS_FOUND = "No Towers found";
	private static final String INVALID_TOWER_ID = "Invalid Tower Id";
	Logger logger = LoggerFactory.getLogger(TowerServiceImpl.class);
	@Autowired
	TowerRepository towerRepository;

	@Autowired
	TowerSkillScoreRepository towerScoreRepository;

	@Autowired
	SkillRepository skillRepository;

	@Override
	public Tower createTower(TowerRequest tower) throws TowerException {
		logger.info("Creating Tower from Tower Service");
		Tower towerResult = towerRepository.findByTowerName(tower.getTowerName());
		if (towerResult != null) {
			logger.error(TOWER_WITH_SAME_NAME_ALREADY_EXISTS);
			throw new TowerException(TOWER_WITH_SAME_NAME_ALREADY_EXISTS);
		}
		Tower tower1 = new Tower();
		tower1.setTowerName(tower.getTowerName());
		return towerRepository.save(tower1);
	}

	@Override
	public List<Tower> getAllTower() throws TowerException {
		logger.info("Getting all Towers from Tower Service");
		List<Tower> towers = towerRepository.findAll();
		if (towers.isEmpty()) {
			logger.error(NO_TOWERS_FOUND);
			throw new TowerException(NO_TOWERS_FOUND);
		}
		return towers;
	}

	@Override
	public String updateTower(TowerRequest tower, Integer towerId) throws TowerException {
		logger.info("Updating Tower from Tower Service");
		if (towerId == null) {
			logger.error(PLEASE_ENTER_TOWER_ID);
			throw new TowerException(PLEASE_ENTER_TOWER_ID);
		}
		Tower towerResult = towerRepository.findByTowerId(towerId);
		if (towerResult == null) {
			logger.error(INVALID_TOWER_ID);
			throw new TowerException(INVALID_TOWER_ID);
		}
		if (towerResult.getTowerName().equals(tower.getTowerName())) {
			logger.error(TOWER_WITH_SAME_NAME_ALREADY_EXISTS);
			throw new TowerException(TOWER_WITH_SAME_NAME_ALREADY_EXISTS);
		}
		towerResult.setTowerName(tower.getTowerName());
		towerRepository.save(towerResult);

		return "Tower name updated";
	}

	@Override
	public List<TowerScoreSkillDetails> addTowerScore(List<TowerScoreRequest> towerScoreRequest) throws TowerException {
		logger.info("Adding the towerScore");
		List<TowerScoreSkillDetails> listResponses = new LinkedList<>();
		for (TowerScoreRequest i : towerScoreRequest) {
			TowerScoreSkillDetails towerScore = towerScoreRepository.findByTowerSkill(i.getTowerId(),
					i.getSkillLevel());
			if (towerScore == null) {
				Skill skill = skillRepository.findBySkillLevel(i.getSkillLevel());
				if (skill == null) {
					logger.error(SKILL_DOESN_T_EXIST);
					throw new TowerException(SKILL_DOESN_T_EXIST + i.getSkillLevel());
				}
				Tower tower = towerRepository.findByTowerId(i.getTowerId());
				if (tower == null) {
					logger.error(TOWER_DOESN_T_EXIST);
					throw new TowerException(TOWER_DOESN_T_EXIST + i.getTowerId());
				}
				TowerScoreSkillDetails towerScoreSkill = new TowerScoreSkillDetails();
				towerScoreSkill.setTower(tower);
				towerScoreSkill.setSkill(skill);
				towerScoreSkill.setMaximumScore(i.getMaximumScore());
				towerScoreSkill.setMinimumScore(i.getMinimumScore());
				towerScoreSkill.setNextLevelMin(i.getNextLevelMin());
				TowerScoreSkillDetails towerScoreResponse = towerScoreRepository.save(towerScoreSkill);
				listResponses.add(towerScoreResponse);
			} else {
				logger.info("Updating the towerScore");
				towerScore.setMaximumScore(i.getMaximumScore());
				towerScore.setMinimumScore(i.getMinimumScore());
				towerScore.setNextLevelMin(i.getNextLevelMin());
				TowerScoreSkillDetails towerScoreResponse = towerScoreRepository.save(towerScore);
				listResponses.add(towerScoreResponse);

			}
		}
		return listResponses;
	}

	@Override
	public List<TowerScoreSkillDetails> getTowerScore(Integer towerId) throws ScoreException {
		logger.info("Getting tower score from Tower service");
		List<TowerScoreSkillDetails> towerList = towerScoreRepository.findByTowerScore(towerId);
		if (towerList.isEmpty()) {
			logger.error(NO_SCORE_DETAILS_IN_THIS_TOWER);
			throw new ScoreException(NO_SCORE_DETAILS_IN_THIS_TOWER);
		}
		return towerList;
	}

	@Override
	public ResponseEntity<TowerScoreSkillDetails> updateTowerScore(Integer towerScoreDetailsId, Integer minimunScore,
			Integer maximumScore, Integer nextLevelMini) throws TowerException {
		logger.info("Updating Tower Score from Tower Service");

		if (towerScoreDetailsId == null) {
			throw new TowerException("Invalid tower id");
		}
		
		if(minimunScore==null)
		{
			throw new TowerException("Please enter minimum score");
		}
		if(maximumScore==null)
		{
			throw new TowerException("Please enter maximum score");
		}
		if(nextLevelMini==null)
		{
			throw new TowerException("Please enter next level score");
		}
		TowerScoreSkillDetails towerScore = towerScoreRepository.getById(towerScoreDetailsId);
		towerScore.setMaximumScore(maximumScore);
		towerScore.setMinimumScore(minimunScore);
		towerScore.setNextLevelMin(nextLevelMini);
		TowerScoreSkillDetails towerScoreResponse = towerScoreRepository.save(towerScore);
		return new ResponseEntity<>(towerScoreResponse, HttpStatus.OK);
	}

	@Override
	public String deleteThreshold(List<Integer> thresholdIds) throws TowerException {

		int count = 0;
		for (Integer id : thresholdIds) {
			TowerScoreSkillDetails towerScoreDetail = towerScoreRepository.findByTowerSkillDetailsId(id);
			towerScoreRepository.delete(towerScoreDetail);
			count++;
		}
		if (count == 0) {
			return "Invalid Threshold Id !";
		}
		return "Deleted Successfully";
	}
}
