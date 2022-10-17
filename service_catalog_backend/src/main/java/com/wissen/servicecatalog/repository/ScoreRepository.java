package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Tower;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {

	@Query("From Score where scoreId=:scoreId")
	Score findByScoreId(Integer scoreId);

	@Query("From Score where employeeMaster.employeeId=:employeeId")
	List<Score> findByEmployeeId(Integer employeeId);

	@Query("From Score where tower.towerName=:towerName")
	List<Score> findByTowerName(String towerName);

	@Query("From Score where project.projectName=:projectName")
	List<Score> findByProjectName(String projectName);

	@Query("From Score where tower.towerName=:towerName And project.projectName=:projectName")
	List<Score> findByTowerNameAndProject(String towerName, String projectName);

	@Query("From Score where project.projectName=:projectName And employeeMaster.employeeId=:empId")
	Score findByProjectNameAndEmployeeId(String projectName, Integer empId);

	@Query("From Score where tower.towerName=:towerName And project.projectName=:projectName And employeeMaster.employeeId=:employeeId")
	List<Score> findByTowerNameAndProjectAndEmployeeId(String towerName, String projectName, Integer employeeId);

	@Query("From Score where employeeMaster.employeeId=:employeeId And quarter=:quarter And activity.activityId=:activityId And project.projectId=:projectId")
	public Score findByEmployeeIdAndQuarterAndActivityId(Integer employeeId, String quarter, Integer activityId,
			Integer projectId);

	List<Score> findByProjectAndTowerAndEmployeeMaster(Project projectDetails, Tower tower,
			EmployeeMaster employeeMaster);

	Score findByEmployeeMasterAndProject(EmployeeMaster employeeMaster, Project projectDetails);

	@Query("From Score where project.projectId=:projectId And tower.towerId=:towerId")
	List<Score> findByProjectIdAndTowerId(Integer projectId, Integer towerId);

	@Query("From Score where employeeMaster.employeeId=:employeeId And tower.towerId=:towerId And project.projectId=:projectId And quarter=:quater And year=:year And status=:status")
	List<Score> findByPublisedScore(Integer employeeId, Integer towerId, Integer projectId, String quater, Integer year,
			String status);

	@Query("From Score where activity.activityId=:activityId And tower.towerId=:towerId And project.projectId=:projectId And employeeMaster.employeeId=:employeeId And quarter=:quater And year=:year And status=:status")
	Score findByActivity(Integer activityId, Integer towerId, Integer projectId, Integer employeeId, String quater,
			int year, String status);

	@Query("From Score where project.projectId=:projectId")
	Score findByProjectId(Integer projectId);

	@Query("From Score where project.projectId=:projectId And tower.towerId=:towerId And status=:status")
	List<Score> findByProjectIdAndTowerId(Integer projectId, Integer towerId, String status);

	@Query("From Score where quarter=:quarter And status=:status And employeeMaster.employeeId=:employeeId And year=:year And tower.towerId=:towerId And project.projectId=:projectId")
	List<Score> findByQuarterAndStatusEmployeeIdAndYearTowerAndProject(String quarter, String status,
			Integer employeeId, int year, Integer towerId, Integer projectId);

	@Query("From Score where tower.towerId=:towerId And project.projectId=:projectId And quarter=:quater And year=:year")
	List<Score> findByPublisedScore(Integer towerId, Integer projectId, String quater, Integer year);

	@Query("From Score where quarter=:quarter And year=:year And status=:status")
	List<Score> findByQuarterAndYearAndStatus(String quarter, int year, String status);

	@Query("From Score where employeeMaster.employeeId=:employeeId And tower.towerId=:towerId And project.projectId=:projectId And quarter=:quarter")
	List<Score> findByEmployeeIdAndTowerAndProject(Integer employeeId, Integer towerId, Integer projectId,
			String quarter);

	List<Score> findAllById(Iterable<Integer> scoreIds);

	@Query("From Score where quarter=:currentQuarter AND year=:year")
	List<Score> findByCurrentQuarter(String currentQuarter, int year);
	
	@Query("From Score where employeeMaster.employeeId=:employeeId And tower.towerId=:towerId And project.projectId=:projectId")
	List<Score> findByEmployeeIdAndTowerAndProject(Integer projectId, Integer towerId, Integer employeeId);
	
}
