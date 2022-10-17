package com.wissen.servicecatalog.service;

import java.util.List;

import com.wissen.servicecatalog.exception.ActivityException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ActivityGlobalChanges;
import com.wissen.servicecatalog.pojo.ActivityRequest;
import com.wissen.servicecatalog.pojo.ActivityResponse;

public interface ActivityService {

	public List<ActivityResponse> addActivity(List<ActivityRequest> activity) throws TowerException;

	public List<ActivityResponse> getTowerId(Integer towerId) throws TowerException;

	List<ActivityResponse> updateActivity(List<ActivityRequest> activityRequest)
			throws ActivityException, TowerException;

	public String deleteActivity(Integer activityId) throws ActivityException;
	public     ActivityGlobalChanges updateGlobalChanges(ActivityGlobalChanges activityGlobalChanges)
            throws TowerException;

}
