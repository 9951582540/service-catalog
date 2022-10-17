package com.wissen.servicecatalog.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.ProjectManagerException;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ManagerScoreResponse;
import com.wissen.servicecatalog.pojo.ProjectResponse;
import com.wissen.servicecatalog.pojo.ScoreUpdateRequest;
import com.wissen.servicecatalog.service.EmployeeService;
import com.wissen.servicecatalog.service.ProjectManagerService;
import com.wissen.servicecatalog.service.ScoreService;
import com.wissen.servicecatalog.service.impl.ProjectServiceImpl;

import io.swagger.annotations.Api;

@Api(tags = "Project Manager Service")
@RestController
@RequestMapping("/service-catalog/projectmanager")
@CrossOrigin(origins = "*", maxAge = 3600) 
public class ProjectManagerController {
	Logger logger = LoggerFactory.getLogger(ProjectManagerController.class);
	@Autowired
	ProjectServiceImpl projectService;

	@Autowired
	ProjectManagerService managerService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	ScoreService scoreService;

	@GetMapping("/get/{managerId}")
	public List<ProjectResponse> getProjects(@PathVariable Integer managerId)
			throws ProjectManagerException, EmployeeException, ProjectException {
		logger.info("Get Projects through Manager Id from Project Manager Controller");
		return managerService.getProjectByManagerId(managerId);

	}

	@GetMapping("/get/Score/{projectId}/{towerId}")
	public List<ManagerScoreResponse> getScore(@PathVariable Integer projectId, @PathVariable Integer towerId)
			throws EmployeeException, ProjectException, TowerException, ScoreException {
		logger.info("Get score through Project ID / Tower ID from Project Manager Controller");
		return managerService.getScoreByProjectAndTower(projectId, towerId);
	}


	@PatchMapping("/score/update")
	public String updateScore(@RequestBody List<ScoreUpdateRequest> scoreUpdateRequest)
			throws ScoreException, EmployeeException {
		logger.info("Update Score from Project Manager Controller");
		return managerService.updateScoreBymanager(scoreUpdateRequest);
	}

	@PutMapping("/reject/Score")
	public String rejectScore(@RequestBody List<Integer> scoreRejected) throws ScoreException, MessagingException, SettingException, ProjectManagerException {
		logger.info("Rejecting score from Project Manager Controller");
		return managerService.rejectScore(scoreRejected);

	}

}
