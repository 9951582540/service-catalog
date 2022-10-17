package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.EmployeeRoleMaster;

public interface EmployeeMasterRepository extends JpaRepository<EmployeeMaster, Integer> {

	EmployeeMaster findByEmployeeId(Integer employeeId);

	EmployeeMaster findByEmailId(String emailId);

	EmployeeMaster findByEmployeeIdAndApplicationRoleMaster(EmployeeMaster employeeMaster,
			ApplicationRoleMaster applicationrolemaster);

	List<EmployeeMaster> findByCurrentEmployeeRoleIdRoleId(Integer EmployeeRoleId);

	EmployeeMaster findByEmployeeIdOrEmployeeName(Integer employeeId, String employeeName);

	List<EmployeeMaster> findByApplicationRoleMasterApplicationName(String ApplicationRoleName);

	List<EmployeeMaster> findByManagerId(EmployeeMaster employeeMaster);

	@Query("From EmployeeMaster where employeeId=:employeeId AND employeeName=:employeeName AND totalExperienceMonths=:totalExperienceMonths AND currentRoleExperienceMonths=:currentRoleExperienceMonths AND managerId=:managerId AND primarySkill=:primarySkill AND secondarySkill=:secondarySkill AND certification=:certification AND emailId=:emailId AND employeeStatus=:employeeStatus AND currentEmployeeRoleId=:currentEmployeeRoleId AND applicationRoleMaster=:applicationRoleMaster")
	EmployeeMaster findByDuplicateEmployee(Integer employeeId, String employeeName, Integer totalExperienceMonths,
			Integer currentRoleExperienceMonths, Integer managerId, String primarySkill, String secondarySkill,
			String certification, String emailId, String employeeStatus, EmployeeRoleMaster currentEmployeeRoleId,
			ApplicationRoleMaster applicationRoleMaster);

}
