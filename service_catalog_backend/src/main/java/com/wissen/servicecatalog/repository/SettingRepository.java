package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wissen.servicecatalog.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting, Integer> {

	@Query("From Setting where name=:name")
	public Setting findBySettingName(String name);

	@Query("From Setting where settingId=:settingId")
	public Setting findBySettingId(Integer settingId);
	

}
