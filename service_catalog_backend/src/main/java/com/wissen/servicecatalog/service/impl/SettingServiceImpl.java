package com.wissen.servicecatalog.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.SettingRequest;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.service.SettingService;

@Service
public class SettingServiceImpl implements SettingService {

	@Autowired
	SettingRepository settingRepository;

	@Override
	public String addSetting(SettingRequest setting) throws SettingException {

		Setting settingName = settingRepository.findBySettingName(setting.getName());

		if (settingName == null) {
			Setting settingNull = new Setting();
			settingNull.setData(setting.getData());
			settingNull.setDescription(setting.getDescription());
			settingNull.setName(setting.getName());
			settingRepository.save(settingNull);
			return "Setting Added";
		} else {

			throw new SettingException("Setting name is already exist!");
		}
	}

	@Override
	public String updateSetting(SettingRequest setting) throws SettingException {

		Setting settingId = settingRepository.findBySettingId(setting.getSettingId());

		if (settingId != null) {

			if (setting.getName().equalsIgnoreCase("service-catalog mail id")
					|| setting.getName().equalsIgnoreCase("Admin Mail id")
					|| setting.getName().equalsIgnoreCase("HR Mail ID")) {

				Pattern regex = Pattern.compile("^[A-Za-z0-9._%+-]+@wisseninfotech.com");

				Matcher matcher = regex.matcher(setting.getData());

				if (matcher.matches()) {
					Setting settingNull = new Setting();
					settingNull.setSettingId(setting.getSettingId());
					settingNull.setData(setting.getData());
					settingNull.setDescription(setting.getDescription());
					settingNull.setName(setting.getName());
					settingRepository.save(settingNull);

					return "Setting is Updated";
				} else {
					throw new SettingException("This field accepts only mail ID");
				}
			} else {
				Setting settingNull = new Setting();
				settingNull.setSettingId(setting.getSettingId());
				settingNull.setData(setting.getData());
				settingNull.setDescription(setting.getDescription());
				settingNull.setName(setting.getName());
				settingRepository.save(settingNull);

				return "Setting is Updated";
			}

		}

		throw new SettingException("Invalid Setting Id!");
	}

	@Override
	public Setting getSetting(Integer settingId) throws SettingException {

		if (settingId == null) {
			throw new SettingException("Please enter the setting id");
		}

		Setting setting = settingRepository.findBySettingId(settingId);
		if (setting == null)
			throw new SettingException("Invalid Setting Id!");
		return setting;
	}

	@Override
	public List<Setting> getAll() throws SettingException {

		List<Setting> list = settingRepository.findAll();
		if (list.isEmpty())
			throw new SettingException("No details found");
		return list;
	}

}
