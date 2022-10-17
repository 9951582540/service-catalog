package com.wissen.servicecatalog.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.exception.ActivityException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ActivityGlobalChanges;
import com.wissen.servicecatalog.pojo.ActivityRequest;
import com.wissen.servicecatalog.pojo.ActivityResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.service.impl.ActivityServiceImpl;

import io.swagger.annotations.Api;

@Api(tags = "Activity Service")
@RestController
@RequestMapping("/service-catalog/activity")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ActivityController {

	Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	ActivityServiceImpl activityService;

	@GetMapping("/get/{towerId}")
	public List<ActivityResponse> getTowerId(@PathVariable Integer towerId) throws TowerException {
		logger.info("getting the Tower Id from Activity Controller");
		return activityService.getTowerId(towerId);
	}

	@PostMapping("/add")
	public List<ActivityResponse> addActivity(@Valid @RequestBody List<ActivityRequest> activity)
			throws TowerException {
		logger.info("Adding the Activity from Activity Controller");
		return activityService.addActivity(activity);
	}

	@PutMapping("/update")
	public List<ActivityResponse> updateActivity(@RequestBody @Valid List<ActivityRequest> activity)
			throws ActivityException, TowerException {
		logger.info("Updating the Activity from Activity Controller");
		return activityService.updateActivity(activity);
	}

	@PostMapping("/upload/{towerName}/{sheetNumber}")
	public String importTowerActivity(@RequestParam("file1") MultipartFile file1, @PathVariable String towerName,
			@PathVariable Integer sheetNumber) throws TowerException, ActivityException, IOException {
		logger.info("Uploading the activity from Activity Controller");
		return activityService.importTowerActivity(file1, towerName, sheetNumber);
	}

	@GetMapping("/download/{towerName}")
	@Async
	public void exportToExcel(HttpServletResponse response, @PathVariable String towerName)
			throws IOException, TowerException, ActivityException {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headervalue = "attachment; filename=Tower_Activity_info.xlsx";
		if (towerName == null)
			throw new TowerException("Please enter the tower name");
		List<Activity> listActivity = activityRepository.findByTowerName(towerName);
		if (listActivity.isEmpty())
			throw new TowerException("Invalid Tower Name");
		ActivityServiceImpl activity = new ActivityServiceImpl(listActivity);
		response.setHeader(headerKey, headervalue);
		activity.export(response);

	}

	@DeleteMapping("/delete/{activityId}")
	public String deleteActivity(@PathVariable Integer activityId) throws ActivityException {
		return activityService.deleteActivity(activityId);
	}

	@PostMapping("/globalchange")
	public ActivityGlobalChanges globalChanges(@RequestBody @Valid ActivityGlobalChanges activityGlobalChanges)
			throws TowerException {

		return activityService.updateGlobalChanges(activityGlobalChanges);

	}
}
