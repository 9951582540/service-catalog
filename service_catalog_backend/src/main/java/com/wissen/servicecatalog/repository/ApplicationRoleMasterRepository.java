package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;

public interface ApplicationRoleMasterRepository extends JpaRepository<ApplicationRoleMaster, Integer> {

	ApplicationRoleMaster findByApplicationName(String name);

}
