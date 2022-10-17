package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wissen.servicecatalog.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {

	public Status findByCurrentEmployeeStatus(String currentEmployeeStatus);

}
