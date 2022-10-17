package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wissen.servicecatalog.entity.Tower;

@Repository
public interface TowerRepository extends JpaRepository<Tower, Integer> {

	Tower findByTowerId(Integer towerId);

	Tower findByTowerName(String towerName);

}
