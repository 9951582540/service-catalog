package com.wissen.servicecatalog.service;

import java.util.List;

import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.SettingRequest;

public interface SettingService {

	public String addSetting(SettingRequest setting) throws SettingException;

	public String updateSetting(SettingRequest setting) throws SettingException;

	public Setting getSetting(Integer settingId) throws SettingException;

	public List<Setting> getAll() throws SettingException;
}
