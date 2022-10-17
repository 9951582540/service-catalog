package com.wissen.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wissen.servicecatalog.entity.OtpDetails;

public interface OtpRepository extends JpaRepository<OtpDetails, Integer> {

	@Query("from OtpDetails where otpNumber=:otpNumber")
	OtpDetails findByOtp(String otpNumber);

	OtpDetails findByEmail(String email);

}
