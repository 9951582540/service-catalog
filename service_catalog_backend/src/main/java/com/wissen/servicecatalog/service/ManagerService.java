package com.wissen.servicecatalog.service;

import java.util.List;

import com.wissen.servicecatalog.entity.Project;

public interface ManagerService {

	List<Project> getProjectByManagerId(String managerId);
}
