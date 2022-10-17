package com.wissen.servicecatalog.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.TowerRequest;
import com.wissen.servicecatalog.pojo.TowerScoreRequest;

public interface TowerService {

	public Tower createTower(TowerRequest tower) throws TowerException;

	public List<Tower> getAllTower() throws TowerException;


	public String updateTower(TowerRequest tower, Integer towerId) throws TowerException;


	 public List<TowerScoreSkillDetails> addTowerScore(List<TowerScoreRequest> towerScoreRequest)
	            throws TowerException;
	
	public List<TowerScoreSkillDetails> getTowerScore(Integer towerId) throws ScoreException;

	public ResponseEntity<TowerScoreSkillDetails> updateTowerScore(Integer towerScoreDetailsId, Integer minimunScore,
			Integer maximumScore, Integer nextLevelMini) throws TowerException;

	public String deleteThreshold(List<Integer> thresholdIds) throws TowerException;
}
