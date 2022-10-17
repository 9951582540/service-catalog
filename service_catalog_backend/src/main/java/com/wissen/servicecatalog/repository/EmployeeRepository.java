package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.wissen.servicecatalog.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	public Employee findByEmployeeId(String employeeId);

	public Employee findByEmployeeIdAndStatus(String employeeId, String status);

	public Employee findByEmail(String email);

	public ResponseEntity<Object> save(String password);

	@Query("SELECT c FROM Employee c WHERE c.resetPasswordToken = ?1")
	public Employee findByResetPasswordToken(String resetPasswordToken);

}
