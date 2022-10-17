package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	List<Project> findByProjectName(String projectName);

	Project findByProjectId(Integer projectId);

	List<Project> findByProjectManagerId(EmployeeMaster projectManagerId);

	@Query("from Project where projectName=:projectName and tower.towerId=:towerId ")
	Project findByProjectNameTowerId(String projectName, Integer towerId);

	@Query("From Project where projectManagerId.employeeId=:projectManagerId AND projectName=:projectName AND tower.towerId=:towerId")
	Project findByProjectManagerIdAndProjectNameAndTowerTowerId(Integer projectManagerId, String projectName,
			Integer towerId);

	@Query("From Project where projectManagerId.employeeId=:projectManagerId AND projectName=:projectName")
	List<Project> findByProjectManagerIdAndProjectName(Integer projectManagerId, String projectName);

	
	@Query("From Project where projectName=:projectName AND tower.towerId=:towerId")
	Project findByProjectNameAndTowerTowerId(String projectName,Integer towerId);
	  @Query("From Project where projectManagerId.employeeId=:managerId AND projectName=:projectName AND tower.towerName=:towerName")
   Project findByProjectManagerIdAndProjectNameAndTowerName(Integer managerId, String projectName,
          String towerName);
}
