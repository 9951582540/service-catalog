package com.wissen.servicecatalog.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.exception.ActivityException;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.PublishScore;
import com.wissen.servicecatalog.pojo.ScoreRequest;
import com.wissen.servicecatalog.pojo.ScoreResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeRoleMasterRepository;
import com.wissen.servicecatalog.repository.FeedbackRepository;
import com.wissen.servicecatalog.repository.ProjectRepository;
import com.wissen.servicecatalog.repository.ScoreRepository;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.StatusRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.repository.TowerSkillScoreRepository;
import com.wissen.servicecatalog.service.ScoreService;
import com.wissen.servicecatalog.util.SendText;

@Service
public class ScoreServiceImpl implements ScoreService {

	private static final String INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE = "Invalid Project Id to assess the score";

	private static final String INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE = "Invalid Employee Id to assess the score";

	private static final String Q4 = "Q4";

	private static final String Q3 = "Q3";

	private static final String Q2 = "Q2";

	private static final String Q1 = "Q1";

	private static final String APPROVED = "Approved";

	private static final String PUBLISHED = "Published";

	private static final String NO_RECORDS = "No records";

	private static final String SUBMITTED = "Submitted";

	private static final String SAVED = "Saved";

	private static final String THERE_IS_NO_DATA_IN_THE_SKILL = "There is no data in the skill";

	Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

	@Autowired
	EmployeeMasterRepository employeeRepository;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	FeedbackRepository feedbackRepository;

	@Autowired
	SettingRepository settingRepository;

	@Autowired
	EmployeeRoleMasterRepository employeeRoleMasterRepository;

	@Autowired
	ApplicationRoleMasterRepository applicationRoleMasterRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	ScoreRepository scoreRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	TowerRepository towerRepository;

	@Autowired
	SendText sendText;

	@Autowired
	TowerSkillScoreRepository towerScoreRepository;

	@Override
	public List<ScoreResponse> saveScore(List<ScoreRequest> scoreRequest, String projectRoleName)
			throws ScoreException {
		logger.info("Saving Score from Score Service");
		if (scoreRequest.isEmpty()) {
			throw new ScoreException("please select the score need to be saved");
		}
		if (projectRoleName == null) {
			throw new ScoreException("please select the skill to save the score");
		}
		List<ScoreResponse> scoreResoponses = new ArrayList<>();
		for (ScoreRequest request : scoreRequest) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);

			EmployeeMaster employeeMasterResult = employeeRepository.findByEmployeeId(request.getEmployeeId());

			if (employeeMasterResult == null) {
				logger.error("Invalid Employee Id to access to score");
				throw new ScoreException(INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE);
			}
			Project projectResult = projectRepository.findByProjectId(request.getProjectId());

			if (projectResult == null) {
				logger.error("Invalid Project Id to access to score");
				throw new ScoreException(INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE);
			}
			Tower towerResult = towerRepository.findByTowerId(request.getTowerId());

			if (towerResult == null) {
				logger.error("Invalid Tower Id to access to score");
				throw new ScoreException("Invalid tower Id to assess the score");
			}
			Activity activityResult = activityRepository.findByActivityId(request.getActivityId());

			if (activityResult == null) {
				logger.error("Invalid Activity Id to access to score");
				throw new ScoreException("Invalid Activity Id to assess the score");
			}
			Skill skill = skillRepository.findBySkillLevel(projectRoleName);
			if (skill == null) {
				logger.error(THERE_IS_NO_DATA_IN_THE_SKILL);
				throw new ScoreException(THERE_IS_NO_DATA_IN_THE_SKILL);

			}
			Score scoreResult = scoreRepository.findByEmployeeIdAndQuarterAndActivityId(request.getEmployeeId(),
					request.getQuarter(), request.getActivityId(), request.getProjectId());

			if (scoreResult == null) {

				Score score = new Score();
				mapper.map(request, score);
				score.setActivity(activityResult);
				score.setEmployeeMaster(employeeMasterResult);
				score.setTower(towerResult);
				score.setProject(projectResult);
				score.setProjectSkill(skill);
				score.setStatus(SAVED);
				score.setProjectSkill(skill);

				Feedback feedback2 = feedbackRepository.findByFeedbackName(SAVED);
				if (feedback2 == null) {

					Feedback feedback = new Feedback();
					feedback.setFeedbackName(SAVED);
					Feedback feedbackResult = feedbackRepository.save(feedback);
					score.setFeedbackMaster(feedbackResult);
					Score scoreSave = scoreRepository.save(score);

					scoreResoponses.add(commonScoreResponse(scoreSave));

				} else {
					score.setFeedbackMaster(feedback2);

					Score scoreSave = scoreRepository.save(score);
					scoreResoponses.add(commonScoreResponse(scoreSave));

				}
			} else if (scoreResult.getStatus().equalsIgnoreCase(SAVED)) {
				scoreResult.setProjectSkill(skill);
				scoreResult.setScore(request.getScore());
				Score scoreSave = scoreRepository.save(scoreResult);
				scoreResoponses.add(commonScoreResponse(scoreSave));

			} else {
				logger.error("Score is already submitted");
				throw new ScoreException("Score as already submitted");
			}
		}
		return scoreResoponses;
	}

	public ScoreResponse commonScoreResponse(Score scoreSave) {
		ScoreResponse scoreResponse = new ScoreResponse();
		scoreResponse.setActivityId(scoreSave.getActivity().getActivityId());
		scoreResponse.setActivityName(scoreSave.getActivity().getActivityName());
		scoreResponse.setCategory(scoreSave.getActivity().getCategory());
		scoreResponse.setFacilitator(scoreSave.getActivity().getFacilitator());
		scoreResponse.setQuarter(scoreSave.getQuarter());
		scoreResponse.setScoreId(scoreSave.getScoreId());
		scoreResponse.setFeedbackName(scoreSave.getFeedbackMaster().getFeedbackName());
		scoreResponse.setScore(scoreSave.getScore());
		scoreResponse.setStatus(scoreSave.getStatus());
		scoreResponse.setTechnologies(scoreSave.getActivity().getTechnologies());
		scoreResponse.setService(scoreSave.getActivity().getService());
		scoreResponse.setYear(scoreSave.getYear());
		scoreResponse.setProjectSkill(scoreSave.getProjectSkill().getSkillLevel());
		return scoreResponse;
	}

	@Override
	public List<ScoreResponse> addScore(List<ScoreRequest> scoreRequest, String projectRoleName)
			throws ScoreException, MessagingException, SettingException {
		logger.info("Adding Score from Score Service");

		if (scoreRequest.isEmpty()) {
			throw new ScoreException("please select the score need to be saved");
		}
		if (projectRoleName == null) {
			throw new ScoreException("please select the skill to save the score");
		}

		List<ScoreResponse> scoreResponses = new LinkedList<>();

		EmployeeMaster manager = null;
		EmployeeMaster employeeMasterResult = null;
		for (ScoreRequest request : scoreRequest) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			employeeMasterResult = employeeRepository.findByEmployeeId(request.getEmployeeId());

			if (employeeMasterResult == null)
				throw new ScoreException(INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE);
			logger.error(INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE);
			Project projectResult = projectRepository.findByProjectId(request.getProjectId());

			if (projectResult == null) {
				logger.error(INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE);
				throw new ScoreException(INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE);
			}
			manager = employeeRepository.findByEmployeeId(projectResult.getProjectManagerId().getEmployeeId());
			if (manager == null) {
				logger.error("Invalid Manager Id or There is no manager Present in the data");
				throw new ScoreException("Invalid Manager Id or There is no manager Present in the data");
			}
			Skill skill = skillRepository.findBySkillLevel(projectRoleName);
			if (skill == null) {
				logger.error(THERE_IS_NO_DATA_IN_THE_SKILL);
				throw new ScoreException(THERE_IS_NO_DATA_IN_THE_SKILL);
			}
			Tower towerResult = towerRepository.findByTowerId(request.getTowerId());
			if (towerResult == null) {
				logger.error("Invalid tower Id to access the score");
				throw new ScoreException("Invalid tower Id to assess the score");
			}
			Activity activityResult = activityRepository.findByActivityId(request.getActivityId());
			if (activityResult == null) {
				logger.error("Invalid Activity Id to access the score");
				throw new ScoreException("Invalid Activity Id to assess the score");
			}

			Status status = statusRepository.findByCurrentEmployeeStatus("Assigned");

			if (status == null) {
				Status st = new Status();
				st.setCurrentEmployeeStatus("Assigned");
				status = statusRepository.save(st);
			}
			Score scoreResult = scoreRepository.findByEmployeeIdAndQuarterAndActivityId(request.getEmployeeId(),
					request.getQuarter(), request.getActivityId(), request.getProjectId());
			if (scoreResult == null) {
				LocalDateTime now = LocalDateTime.now();
				Score score = new Score();
				mapper.map(request, score);
				score.setProjectSkill(skill);
				score.setTimeLine(now);
				score.setActivity(activityResult);
				score.setEmployeeMaster(employeeMasterResult);
				score.setTower(towerResult);
				score.setProject(projectResult);
				score.setStatus(SUBMITTED);
				score.setRoadMap(SUBMITTED);
				score.setCurrentEmployeeStatus(status);
				Feedback feedback2 = feedbackRepository.findByFeedbackName(SUBMITTED);
				if (feedback2 == null) {
					Feedback feedback = new Feedback();
					feedback.setFeedbackName(SUBMITTED);
					Feedback feedbackResult = feedbackRepository.save(feedback);
					score.setFeedbackMaster(feedbackResult);
					Score scoreSave = scoreRepository.save(score);
					scoreResponses.add(commonScoreResponse(scoreSave));
				} else {
					score.setFeedbackMaster(feedback2);
					score.setTimeLine(now);
					score.setRoadMap(SUBMITTED);
					score.setProjectSkill(skill);
					Score scoreSave = scoreRepository.save(score);
					scoreResponses.add(commonScoreResponse(scoreSave));
				}
			} else {
				if (scoreResult.getStatus().equalsIgnoreCase(SUBMITTED)
						|| scoreResult.getStatus().equalsIgnoreCase(PUBLISHED)
						|| scoreResult.getStatus().equalsIgnoreCase(APPROVED)) {
					logger.error("score is already submitted you cant update it");
					throw new ScoreException("score is already submitted you cant update it");
				}
				scoreResult.setScore(request.getScore());
				scoreResult.setProjectSkill(skill);
				scoreResult.setStatus(SUBMITTED);
				scoreResult.setRoadMap(SUBMITTED);
				scoreResult.setTimeLine(LocalDateTime.now());
				scoreResult.setCurrentEmployeeStatus(status);
				Score scoreSave = scoreRepository.save(scoreResult);
				scoreResponses.add(commonScoreResponse(scoreSave));
			}
		}

		sendScoreMail(manager, employeeMasterResult);

		return scoreResponses;
	}

	public void sendScoreMail(EmployeeMaster manager, EmployeeMaster employeeMasterResult)
			throws SettingException, ScoreException {
		String sub = "Assessment Score";
		String msg = "<html>" + "<head>" + "<h2 style=font-family:'Lato Regular'; text-align:'center;'> Service Catalog"
				+ "</h2>" + "<body>" + "Hello " + manager.getEmployeeName() + "<br>" + "<br>"
				+ "Please Approve the Employee Score " + "<br>" + "Name: " + employeeMasterResult.getEmployeeName()
				+ "<br>" + "Employee Id: " + employeeMasterResult.getEmployeeId() + "<br>" + "<br>"
				+ "has Submitted there score Please check and approve" + "<br>" + "<br>" + "Regards" + "<br>" + "Admin"
				+ "</head>" + "</body>" + "</html>";
		Setting findBySettingMailId = settingRepository.findBySettingName("service-catalog mail id");
		if (findBySettingMailId == null)
			throw new SettingException("Please add the settings for Service catalog mail id");

		Setting findBySettingMailPassword = settingRepository.findBySettingName("service-catalog mail id password");

		if (findBySettingMailPassword == null)
			throw new SettingException("Please add the settings for Service catalog password id");

		try {
			String mail[] = { manager.getEmailId() };
			sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, sub, msg);
		} catch (Exception e) {

			logger.error("Failed To send The Mail !,due to port issue", e);
			throw new ScoreException("Failed to send email due to error : ".concat(e.getMessage()));

		}

	}

	@Override
	public List<ScoreResponse> getScore(Integer employeeId) throws ScoreException {
		logger.info("Getting score from Score Service");
		if (employeeId == null) {
			logger.error(NO_RECORDS);
			throw new ScoreException(NO_RECORDS);
		}
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);

		List<Score> scoreList = scoreRepository.findByEmployeeId(employeeId);
		if (scoreList.isEmpty()) {
			logger.error(NO_RECORDS);
			throw new ScoreException(NO_RECORDS);
		}
		List<ScoreResponse> reponseList = new ArrayList<>();
		for (Score score : scoreList) {
			ScoreResponse scoreResponse = new ScoreResponse();
			mapper.map(score, scoreResponse);
			scoreResponse.setSkill(score.getActivity().getSkill().getSkillLevel());
			scoreResponse.setCategory(score.getActivity().getCategory());
			scoreResponse.setService(score.getActivity().getService());
			scoreResponse.setFacilitator(score.getActivity().getFacilitator());
			scoreResponse.setTechnologies(score.getActivity().getTechnologies());
			scoreResponse.setPrimarySkill(score.getEmployeeMaster().getPrimarySkill());
			scoreResponse.setSecondarySkill(score.getEmployeeMaster().getSecondarySkill());
			scoreResponse.setFeedbackName(score.getFeedbackMaster().getFeedbackName());
			reponseList.add(scoreResponse);
		}

		return reponseList;

	}

	@Override
	public List<ScoreResponse> getActivity(Integer employeeId, Integer projectId, Integer towerId)
			throws ActivityException {
		logger.info("Getting activity from Score Service");
		Project project = projectRepository.findByProjectId(projectId);
		if (project == null) {
			logger.error("The project doesn't exists");
			throw new ActivityException("The Project Doesn't exists");
		}
		List<Activity> activitys = activityRepository.getActivity(towerId);
		if (activitys.isEmpty()) {
			logger.error("No activities for this tower");
			throw new ActivityException("No activities for this tower");
		}
		Tower findByTowerId = towerRepository.findByTowerId(towerId);
		List<TowerScoreSkillDetails> towerScore = towerScoreRepository.findByTower(findByTowerId);
		if (towerScore.isEmpty())
			throw new ActivityException("No scores for this tower,Cant assess the activity");

		String quater = null;

		List<ScoreResponse> scoreResponsResult = new ArrayList<>();
		LocalDate date = LocalDate.now();
		int month = date.getMonthValue();
		int year = date.getYear();
		if (month >= 1 && month <= 3) {
			quater = Q1;
		} else if (month >= 4 && month <= 6) {
			quater = Q2;
		} else if (month >= 7 && month < 10) {
			quater = Q3;
		} else {
			quater = Q4;
		}
		for (Activity activityResult : activitys) {

			Score scoreDraft = scoreRepository.findByActivity(activityResult.getActivityId(), towerId, projectId,
					employeeId, quater, year, SAVED);
			Score score = scoreRepository.findByActivity(activityResult.getActivityId(), towerId, projectId, employeeId,
					quater, year, SUBMITTED);
			if (score != null) {
				ScoreResponse scoreResponse = new ScoreResponse();
				scoreResponse.setSkill(score.getActivity().getSkill().getSkillLevel());
				scoreResponse.setActivityId(score.getActivity().getActivityId());
				scoreResponse.setActivityName(score.getActivity().getActivityName());
				scoreResponse.setCategory(score.getActivity().getCategory());
				scoreResponse.setFacilitator(score.getActivity().getFacilitator());
				scoreResponse.setService(score.getActivity().getService());
				scoreResponse.setTechnologies(score.getActivity().getTechnologies());
				scoreResponse.setScore(score.getScore());
				scoreResponse.setStatus(score.getStatus());
				scoreResponse.setQuarter(score.getQuarter());
				scoreResponse.setYear(score.getYear());
				scoreResponse.setScoreId(score.getScoreId());
				scoreResponsResult.add(scoreResponse);

			} else if (scoreDraft != null) {
				ScoreResponse scoreResponse = new ScoreResponse();
				scoreResponse.setSkill(scoreDraft.getActivity().getSkill().getSkillLevel());
				scoreResponse.setActivityId(scoreDraft.getActivity().getActivityId());
				scoreResponse.setActivityName(scoreDraft.getActivity().getActivityName());
				scoreResponse.setCategory(scoreDraft.getActivity().getCategory());
				scoreResponse.setFacilitator(scoreDraft.getActivity().getFacilitator());
				scoreResponse.setService(scoreDraft.getActivity().getService());
				scoreResponse.setTechnologies(scoreDraft.getActivity().getTechnologies());
				scoreResponse.setScore(scoreDraft.getScore());
				scoreResponse.setStatus(scoreDraft.getStatus());
				scoreResponse.setQuarter(scoreDraft.getQuarter());
				scoreResponse.setYear(scoreDraft.getYear());
				scoreResponse.setScoreId(scoreDraft.getScoreId());
				scoreResponsResult.add(scoreResponse);
			}

			else {
				ScoreResponse scoreResponse = new ScoreResponse();
				scoreResponse.setSkill(activityResult.getSkill().getSkillLevel());
				scoreResponse.setActivityId(activityResult.getActivityId());
				scoreResponse.setActivityName(activityResult.getActivityName());
				scoreResponse.setCategory(activityResult.getCategory());
				scoreResponse.setFacilitator(activityResult.getFacilitator());
				scoreResponse.setService(activityResult.getService());
				scoreResponse.setTechnologies(activityResult.getTechnologies());
				scoreResponse.setScore(0);
				scoreResponse.setStatus("Not Added");
				scoreResponse.setQuarter(quater);
				scoreResponse.setYear(year);
				scoreResponsResult.add(scoreResponse);

			}

		}
		return scoreResponsResult;
	}

	@Override
	public List<ScoreResponse> getScoreByTowerAndProject(String toweName, String projectName, Integer employeeId)
			throws ScoreException {
		logger.info("Getting score by Tower and Project from ");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<Score> score = scoreRepository.findByTowerNameAndProjectAndEmployeeId(toweName, projectName, employeeId);
		if (score.isEmpty()) {
			logger.error("The employee doesn't have any data to display");
			throw new ScoreException("The employe doesn't have any data to display");
		}
		List<ScoreResponse> employeeResponseList = new ArrayList<>();
		for (Score scoreList : score) {
			ScoreResponse scoreDetailsResponse = new ScoreResponse();
			modelMapper.map(scoreList, scoreDetailsResponse);
			scoreDetailsResponse.setActivityName(scoreList.getActivity().getActivityName());
			scoreDetailsResponse.setCategory(scoreList.getActivity().getCategory());
			scoreDetailsResponse.setFacilitator(scoreList.getActivity().getFacilitator());
			scoreDetailsResponse.setService(scoreList.getActivity().getService());
			scoreDetailsResponse.setPrimarySkill(scoreList.getEmployeeMaster().getPrimarySkill());
			scoreDetailsResponse.setSecondarySkill(scoreList.getEmployeeMaster().getSecondarySkill());

			scoreDetailsResponse.setTechnologies(scoreList.getActivity().getTechnologies());
			scoreDetailsResponse.setScore(scoreList.getScore());
			employeeResponseList.add(scoreDetailsResponse);
		}
		return employeeResponseList;
	}

	@Override
	public String publishScore(List<PublishScore> scoreId) throws ScoreException {
		logger.info("Publising Score from Score Service");

		List<Score> saveList = new LinkedList<>();

		scoreId.forEach(i -> {
			Score findByScoreId = scoreRepository.findByScoreId(i.getScoreId());

			if (findByScoreId.getStatus().equalsIgnoreCase(SUBMITTED)
					|| findByScoreId.getStatus().equalsIgnoreCase(APPROVED)) {
				findByScoreId.setScore(i.getScore());
				findByScoreId.setStatus(PUBLISHED);
				saveList.add(findByScoreId);
			}
		});

		List<Score> saveAll = scoreRepository.saveAll(saveList);
		if (saveAll.isEmpty())
			throw new ScoreException("Scores are already published");

		return "Score Published";

	}

	@Override
	public List<ScoreResponse> getScoreByTowerProjectQuarterYear(Integer employeeId, Integer towerId, Integer projectId,
			String quarter, int year) throws ScoreException {
		logger.info("Getting score by tower project Quarter Year from Score Service");

		List<Score> scoreList = scoreRepository.findByPublisedScore(employeeId, towerId, projectId, quarter, year,
				PUBLISHED);
		if (scoreList.isEmpty()) {
			logger.error(NO_RECORDS);
			throw new ScoreException(NO_RECORDS);
		}
		return scoreList.stream()
				.map(score -> new ScoreResponse(score.getScoreId(), score.getActivity().getActivityId(),
						score.getActivity().getCategory(), score.getActivity().getService(),
						score.getActivity().getTechnologies(), score.getActivity().getActivityName(),
						score.getEmployeeMaster().getPrimarySkill(), score.getEmployeeMaster().getSecondarySkill(),
						score.getActivity().getFacilitator(), score.getScore(), score.getStatus(), score.getQuarter(),
						score.getYear(), score.getActivity().getSkill().getSkillLevel(),
						score.getFeedbackMaster().getFeedbackName(), score.getProjectSkill().getSkillLevel()))
				.collect(Collectors.toList());
	}

	@Override
	public List<ScoreResponse> getScoresByTowerProjectQuarterYear(Integer towerId, Integer projectId, String quarter,
			int year, boolean bool) throws ScoreException {
		logger.info("Getting score by tower project Quarter Year from Score Service");

		if (bool) {

			List<Score> scoreFilter = scoreRepository.findByPublisedScore(towerId, projectId, quarter, year);
			if (scoreFilter.isEmpty()) {
				logger.error("There is no data to display");
				throw new ScoreException("There is no score for this quarter/tower/project");
			}
			return scoreFilter.stream().filter(score -> score.getStatus().equalsIgnoreCase(SUBMITTED)
					|| score.getStatus().equalsIgnoreCase(APPROVED) || score.getStatus().equalsIgnoreCase(PUBLISHED))
					.map(score -> new ScoreResponse(score.getScoreId(), score.getActivity().getActivityId(),
							score.getActivity().getCategory(), score.getActivity().getService(),
							score.getActivity().getTechnologies(), score.getActivity().getActivityName(),
							score.getEmployeeMaster().getPrimarySkill(), score.getEmployeeMaster().getSecondarySkill(),
							score.getActivity().getFacilitator(), score.getScore(), score.getStatus(),
							score.getQuarter(), score.getYear(), score.getActivity().getSkill().getSkillLevel(),
							score.getFeedbackMaster().getFeedbackName(), score.getProjectSkill().getSkillLevel()))
					.collect(Collectors.toList());
		} else {

			String currentQuarter = null;
			LocalDate date = LocalDate.now();
			int month = date.getMonthValue();
			if (month >= 1 && month <= 3) {
				currentQuarter = Q1;

			} else if (month >= 4 && month <= 6) {
				currentQuarter = Q2;

			} else if (month >= 7 && month < 10) {
				currentQuarter = Q3;
			} else {
				currentQuarter = Q4;
			}
			List<Score> scoreList = scoreRepository.findByCurrentQuarter(currentQuarter, date.getYear());
			if (scoreList.isEmpty()) {
				logger.error("There is no data to display");
				throw new ScoreException("There is no score's for current quarter");
			}
			return scoreList.stream().filter(score -> score.getStatus().equalsIgnoreCase(SUBMITTED)
					|| score.getStatus().equalsIgnoreCase(APPROVED) || score.getStatus().equalsIgnoreCase(PUBLISHED))
					.map(score -> new ScoreResponse(score.getScoreId(), score.getActivity().getActivityId(),
							score.getActivity().getCategory(), score.getActivity().getService(),
							score.getActivity().getTechnologies(), score.getActivity().getActivityName(),
							score.getEmployeeMaster().getPrimarySkill(), score.getEmployeeMaster().getSecondarySkill(),
							score.getActivity().getFacilitator(), score.getScore(), score.getStatus(),
							score.getQuarter(), score.getYear(), score.getActivity().getSkill().getSkillLevel(),
							score.getFeedbackMaster().getFeedbackName(), score.getProjectSkill().getSkillLevel()))
					.collect(Collectors.toList());
		}
	}
}