package com.wissen.servicecatalog.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.SettingRequest;
import com.wissen.servicecatalog.service.SettingService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/service-catalog/setting")
@Api(tags = "Setting Service")
@CrossOrigin(origins = "*", maxAge = 3600) 
public class SettingController {
	Logger logger = LoggerFactory.getLogger(SettingController.class);
	

	@Autowired
	SettingService settingService;

	@PostMapping("/add")
	public String addSetting(@RequestBody @Valid SettingRequest setting) throws SettingException {
		logger.info("Adding setting from Setting Controller");
		return settingService.addSetting(setting);
	}

	@PutMapping("/update")
	public String updateSetting(@RequestBody @Valid SettingRequest setting) throws SettingException {
		logger.info("Updating setting from Setting Controller");
		return settingService.updateSetting(setting);
	}

	@GetMapping("/get/{settingId}")
	public Setting getSetting(@PathVariable Integer settingId) throws SettingException {
		logger.info("Getting setting by entering setting id from Setting Controller");
		return settingService.getSetting(settingId);
	}

	@GetMapping("/getAll")
	public List<Setting> getAll() throws SettingException {
		logger.info("Getting all settings from Setting Controller");
		return settingService.getAll();
	}
}
