package com.wissen.servicecatalog.service;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.ProjectManagerException;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ManagerScoreResponse;
import com.wissen.servicecatalog.pojo.ProjectResponse;
import com.wissen.servicecatalog.pojo.ScoreUpdateRequest;

@Service
public interface ProjectManagerService {

	List<ProjectResponse> getProjectByManagerId(Integer managerId)
			throws ProjectManagerException, EmployeeException, ProjectException;


	public List<ManagerScoreResponse> getScoreByProjectAndTower(Integer projectd, Integer towerId)
			throws EmployeeException, ProjectException, TowerException, ScoreException;

	public String updateScoreBymanager(List<ScoreUpdateRequest> scoreUpdateRequest)
			throws EmployeeException, ScoreException;



	public String rejectScore(List<Integer> scoreIds) throws ScoreException, MessagingException, SettingException, ProjectManagerException;


}
