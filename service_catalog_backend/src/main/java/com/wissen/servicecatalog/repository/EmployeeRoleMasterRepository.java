package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.servicecatalog.entity.EmployeeRoleMaster;

public interface EmployeeRoleMasterRepository extends JpaRepository<EmployeeRoleMaster, Integer> {

	EmployeeRoleMaster findByEmployeeRoleName(String name);
}
