package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;

@Repository
public interface TowerSkillScoreRepository extends JpaRepository<TowerScoreSkillDetails, Integer> {

	@Query("From TowerScoreSkillDetails where tower.towerId=:towerId And skill.skillLevel=:skillLevel")
	TowerScoreSkillDetails findByTowerSkill(Integer towerId, String skillLevel);

	@Query("From TowerScoreSkillDetails where tower.towerId=:towerId")
	List<TowerScoreSkillDetails> findByTowerScore(Integer towerId);

	TowerScoreSkillDetails findByTowerSkillDetailsId(Integer towerSkillDetailsId);
	
//	TowerScoreSkillDetails findByTower(Tower tower);
	
	List<TowerScoreSkillDetails> findByTower(Tower tower);
	
	

}
