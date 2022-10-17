package com.wissen.servicecatalog.service;

import java.util.List;

import javax.mail.MessagingException;

import com.wissen.servicecatalog.exception.ActivityException;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.PublishScore;
import com.wissen.servicecatalog.pojo.ScoreRequest;
import com.wissen.servicecatalog.pojo.ScoreResponse;

public interface ScoreService {

	public List<ScoreResponse> saveScore(List<ScoreRequest> scoreRequest, String projectRoleName) throws ScoreException;

	public List<ScoreResponse> addScore(List<ScoreRequest> scoreRequest, String projectRoleName)
			throws ScoreException, MessagingException, SettingException;


	public List<ScoreResponse> getScore(Integer employeeId) throws ScoreException;

	public List<ScoreResponse> getScoreByTowerAndProject(String toweName, String projectName, Integer employeeId)
			throws ScoreException;

	public List<ScoreResponse> getActivity(Integer employeeId, Integer projectId, Integer towerId)
			throws ActivityException;


	public String publishScore(List<PublishScore> scoreId) throws ScoreException;

	public List<ScoreResponse> getScoreByTowerProjectQuarterYear(Integer employeeId, Integer towerId, Integer projectId,
			String quater, int year) throws ScoreException;

	public List<ScoreResponse> getScoresByTowerProjectQuarterYear(Integer towerId, Integer projectId, String quarter,
			int year,boolean bool) throws ScoreException;

}
