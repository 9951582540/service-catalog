package com.wissen.servicecatalog.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ProjectRequest;
import com.wissen.servicecatalog.pojo.ProjectResponse;

public interface ProjectService {

	public String addProject(ProjectRequest projectrequest) throws ProjectException, EmployeeException, TowerException;

	public ProjectResponse getProject(String projectId) throws ProjectException;

	public Set<ProjectResponse> getAllProject() throws ProjectException;
	
	public String updateProject(String projectName, Integer managerId)
			throws ProjectException, EmployeeException, TowerException ;
	
	public ResponseEntity<Object> importProjectDetails(MultipartFile file)
            throws FileNotFoundException, ProjectException, MessagingException, EmployeeException, TowerException, IOException;

}
