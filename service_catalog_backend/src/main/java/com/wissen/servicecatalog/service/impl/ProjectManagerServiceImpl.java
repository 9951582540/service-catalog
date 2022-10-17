package com.wissen.servicecatalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.ProjectManagerException;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ManagerScoreResponse;
import com.wissen.servicecatalog.pojo.ProjectResponse;
import com.wissen.servicecatalog.pojo.ScoreUpdateRequest;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.FeedbackRepository;
import com.wissen.servicecatalog.repository.ProjectActivityRepository;
import com.wissen.servicecatalog.repository.ProjectRepository;
import com.wissen.servicecatalog.repository.ScoreRepository;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.StatusRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.repository.TowerSkillScoreRepository;
import com.wissen.servicecatalog.service.ProjectManagerService;
import com.wissen.servicecatalog.util.SendText;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {

	Logger logger = LoggerFactory.getLogger(ProjectManagerServiceImpl.class);
	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	SettingRepository settingRepository;

	@Autowired
	EmployeeMasterRepository employeeMasterRepository;

	@Autowired
	TowerRepository towerRepository;

	@Autowired
	ScoreRepository scoreRepository;

	@Autowired
	SkillRepository repository;

	@Autowired
	TowerSkillScoreRepository skillScoreRepository;

	@Autowired
	TowerSkillScoreRepository towerScoreRepository;

	@Autowired
	ApplicationRoleMasterRepository applicationRoleMasterRepository;

	@Autowired
	ProjectActivityRepository projectactivityRepository;

	@Autowired
	FeedbackRepository feedbackRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	SendText sendText;

	@Override
	public List<ProjectResponse> getProjectByManagerId(Integer managerId)
			throws ProjectManagerException, EmployeeException, ProjectException {
		logger.info("Getting Project By Manager Id from Project Manager Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<ProjectResponse> list = new ArrayList<>();

		EmployeeMaster employeeMaster = employeeMasterRepository.findByEmployeeId(managerId);

		if (employeeMaster == null) {
			logger.error("Invalid Employee Id");
			throw new EmployeeException("Invalid Employee Id");

		}
		if (!employeeMaster.getApplicationRoleMaster().getApplicationName().equalsIgnoreCase("Project Manager")) {
			logger.error("Employee Id is not project manager");
			throw new EmployeeException("Employee Id is not project manager");

		}

		List<Project> project = projectRepository.findByProjectManagerId(employeeMaster);

		if (project == null) {
			logger.error("No projects found in this employee Id");
			throw new ProjectException("No projects found in this employee Id");

		}
		for (Project result : project) {
			ProjectResponse projectResponse = new ProjectResponse();

			projectResponse.setProjectId(result.getProjectId());
			projectResponse.setProjectName(result.getProjectName());
			projectResponse.setProjectManagerId(managerId);
			list.add(projectResponse);
		}
		return list;
	}

	@Override
	public String updateScoreBymanager(List<ScoreUpdateRequest> scoreUpdateRequest)
			throws EmployeeException, ScoreException {
		logger.info("Updating Score from Project Manager Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		for (ScoreUpdateRequest scoreRequest : scoreUpdateRequest) {

			Skill skill = repository.findBySkillId(scoreRequest.getProjectSkillId());
			if (skill == null) {
				logger.error("skill is not present");
				throw new ScoreException("skill is not present ");

			}
			Score scoreDetails = scoreRepository.findByScoreId(scoreRequest.getScoreId());
			if (scoreDetails == null) {
				logger.error("Invalid score id");
				throw new ScoreException("Invalid score id");

			}
			ManagerScoreResponse scoreResponse = new ManagerScoreResponse();

			modelMapper.map(scoreDetails, scoreResponse);

			scoreDetails.setScore(scoreRequest.getScore());
			scoreDetails.setProjectSkill(skill);

			if (scoreDetails.getStatus().equalsIgnoreCase("Published")) {
				logger.error("Score is already Published, You can't approve it");
				throw new ScoreException("Score is already Published, You can't approve it");

			}
			if (scoreDetails.getStatus().equalsIgnoreCase("Approved")) {
				logger.error("please enter the valid projectId");
				throw new ScoreException("Score is already Approved");

			}
			scoreDetails.setStatus("Approved");
			scoreDetails.setRoadMap(scoreRequest.getRoadMap());
			Status currentEmployeeStatus = statusRepository
					.findByCurrentEmployeeStatus(scoreRequest.getEmployeeStatus());
			if (currentEmployeeStatus == null) {
				Status status = new Status();
				status.setCurrentEmployeeStatus(scoreRequest.getEmployeeStatus());
				statusRepository.save(status);
				scoreDetails.setCurrentEmployeeStatus(status);
			}
			scoreDetails.setCurrentEmployeeStatus(currentEmployeeStatus);

			Feedback feedback2 = feedbackRepository.findByFeedbackName(scoreRequest.getFeedback());
			if (feedback2 == null) {
				Feedback feedback = new Feedback();
				feedback.setFeedbackName(scoreRequest.getFeedback());
				feedbackRepository.save(feedback);
				scoreDetails.setFeedbackMaster(feedback);
				scoreRepository.save(scoreDetails);

			} else {
				scoreDetails.setFeedbackMaster(feedback2);
				scoreDetails.setRoadMap(scoreRequest.getRoadMap());
				scoreRepository.save(scoreDetails);
			}

		}
		return "score updated";

	}

	@Override
	public List<ManagerScoreResponse> getScoreByProjectAndTower(Integer projectId, Integer towerId)
			throws EmployeeException, ProjectException, TowerException, ScoreException {
		logger.info("Getting Score by Project and Tower from Project Manager Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		Project project = projectRepository.findByProjectId(projectId);
		if (project == null) {
			logger.error("Invalid project id");
			throw new ProjectException("Invalid project Id");

		}
		if (projectId == null) {
			logger.error("please enter the valid projectId");
			throw new ProjectException("please enter the valid projectId");

		}
		Tower tower = towerRepository.findByTowerId(towerId);
		if (tower == null) {
			logger.error("Invalid tower id");
			throw new TowerException("Invalid tower Id");

		}
		if (towerId == null) {
			logger.error("please enter the valid projectId");
			throw new TowerException("please enter the valid projectId");

		}
		List<Score> scores = scoreRepository.findByProjectIdAndTowerId(projectId, towerId);

		List<ManagerScoreResponse> reponseList = new ArrayList<>();

		int sum = 0;
		for (Score score : scores) {

			if (score.getStatus().equalsIgnoreCase("Submitted") || score.getStatus().equalsIgnoreCase("Approved")) {
				ManagerScoreResponse scoreResponse = new ManagerScoreResponse();
				modelMapper.map(score, scoreResponse);

				scoreResponse.setEmployeeId(score.getEmployeeMaster().getEmployeeId());
				List<Score> scores1 = scoreRepository.findByEmployeeIdAndTowerAndProject(projectId, towerId,
						score.getEmployeeMaster().getEmployeeId());
				for (Score sc : scores1) {
					sc.getScore();
					sum += sc.getScore();
				}
				scoreResponse.setCategory(score.getActivity().getCategory());
				scoreResponse.setService(score.getActivity().getService());
				scoreResponse.setFacilitator(score.getActivity().getFacilitator());
				scoreResponse.setTechnologies(score.getActivity().getTechnologies());
				scoreResponse.setPrimarySkill(score.getEmployeeMaster().getPrimarySkill());
				scoreResponse.setSecondarySkill(score.getEmployeeMaster().getSecondarySkill());
				scoreResponse.setTowerName(score.getTower().getTowerName());
				scoreResponse.setActivityName(score.getActivity().getActivityName());

				TowerScoreSkillDetails findByTowerScore = skillScoreRepository.findByTowerSkill(towerId,
						score.getProjectSkill().getSkillLevel());

				if (findByTowerScore == null)
					throw new ScoreException("threshold score is empty");

				scoreResponse.setMinimumScore(findByTowerScore.getMinimumScore());

				scoreResponse.setMaximumScore(findByTowerScore.getMaximumScore());
				scoreResponse.setNextLevelMin(findByTowerScore.getNextLevelMin());
				scoreResponse.setTimeLine(score.getTimeLine() + "");
				scoreResponse.setStatus(score.getStatus());
				scoreResponse.setProjectrole(score.getProjectSkill().getSkillLevel());
				scoreResponse.setCurrentemployeestatus(score.getCurrentEmployeeStatus().getCurrentEmployeeStatus());

				if (sum < findByTowerScore.getMinimumScore()) {
					scoreResponse.setFeedbackName("Low competence");
				}
				if (sum > findByTowerScore.getMinimumScore() && sum < findByTowerScore.getMaximumScore()) {
					scoreResponse.setFeedbackName("Adaquate competence");
				}
				if (sum > findByTowerScore.getMaximumScore() && sum < findByTowerScore.getNextLevelMin()) {
					scoreResponse.setFeedbackName("Over Qualified");
				}
				if (sum > findByTowerScore.getMaximumScore() && sum > findByTowerScore.getNextLevelMin()) {
					scoreResponse.setFeedbackName("Qualified for next level");
				}
				reponseList.add(scoreResponse);
			}
		}
		return reponseList;
	}

	@Override
	public String rejectScore(List<Integer> scoreIds) throws ScoreException, MessagingException, SettingException, ProjectManagerException {
		if (scoreIds == null || scoreIds.isEmpty())
			throw new ScoreException("please enter score id");

		List<Score> scorelist = new ArrayList<>();
		for (Integer i : scoreIds) {
			Score score = scoreRepository.findByScoreId(i);
			if (score == null) {
				throw new ScoreException("invalid score id " + i);
			}
			if (score.getStatus().equalsIgnoreCase("Submitted")) {

				score.setStatus("Saved");
				scorelist.add(score);
			}

		}
		if (scorelist.isEmpty())
			throw new ScoreException("No data found ");
		scoreRepository.saveAll(scorelist);

		int count = 0;
		String subject = "Score is rejected ";
		String body = "<html>" + "<head>"
				+ "<h2 style=font-family:'Lato Regular'; text-align:'center;'> Service Catalog" + "</h2>"
				+ "<p style=font-family:'Lato Regular'; 'font-size:'11px;'>Hello, "
				+ scorelist.get(0).getEmployeeMaster().getEmployeeName()
				+ "</p> <p style=font-family:'Lato Regular'; font-size:'11px;'>"
				+ "Following Service Catalog Scores for :-" + "<br></br>" + "Tower :- "
				+ scorelist.get(0).getTower().getTowerName() + "<br>" + "Project :- "
				+ scorelist.get(0).getProject().getProjectName() + "<br>"

				+ "Has been rejected by " + scorelist.get(0).getProject().getProjectManagerId().getEmployeeName()
				+ "<br></br>" + " <table border ='1' 'solid' '#404040' style='border-collapse: collapse';>" + "<tr>"
				+ "<th style= 'margin:0;padding-left:8px; font-size:'6px;' font-family:'Lato Regular';font-weight:'bold'>S.no.</th>"
				+ "<th style='margin:0;padding-left:8px;font-family:'Lato Regular';font-weight:'bold'>Service</th>"
				+ "<th style='margin:0;padding-left:8px;font-family:'Lato Regular';font-weight:'bold'>Category</th>"
				+ "<th style='margin:0;padding-left:8px;font-family:'Lato Regular';font-weight:'bold'>Activity</th> "
				+ "</tr> ";

		for (Score sr : scorelist) {
			count++;
			body = body.concat("<tr>" + "<td>" + count + "</td>" + "<td>" + sr.getActivity().getService() + "</td>"
					+ "<td>" + sr.getActivity().getCategory() + "</td>" + "<td>" + sr.getActivity().getActivityName()
					+ "</td>" + "</tr>");

		}
		body = body.concat("</table>" + "" + "<p>" + "Please update your Score Again " + "<br></br>" + "Regards</p>"
				+ "<p>Admin</p>" + "</head>" + "</html>");

		Setting findBySettingMailId = settingRepository.findBySettingName("service-catalog mail id");
		if (findBySettingMailId == null)
			throw new SettingException("Please add the settings for Service catalog mail id");

		Setting findBySettingMailPassword = settingRepository.findBySettingName("service-catalog mail id password");

		if (findBySettingMailPassword == null)
			throw new SettingException("Please add the settings for Service catalog mail id");

		try {
		    String mail[]= {scorelist.get(0).getEmployeeMaster().getEmailId()};
			sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(),
				mail, subject, body);
		} catch (Exception e) {
		     logger.error("Failed To send The Mail !,due to port issue", e);
		            throw new ProjectManagerException("Failed to send email due to error : ".concat(e.getMessage()));
			}

		return "Score Sucessfully Rejected";
	}

}
