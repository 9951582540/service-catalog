package com.wissen.servicecatalog.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.security.auth.login.LoginException;

import org.springframework.http.ResponseEntity;

import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.EmployeeRegister;
import com.wissen.servicecatalog.pojo.EmployeeRegisterRequest;

public interface LoginService {

	Employee userRegister(EmployeeRegisterRequest employee) throws EmployeeException, MessagingException,SettingException, IOException, LoginException;


	ResponseEntity<Object> sendEmailtoUser(String email)
			throws UnsupportedEncodingException, IOException, EmployeeException;
	ResponseEntity<Object> sendEmailtoRegisteredEmployee(String email, String link)
			throws UnsupportedEncodingException,SettingException, IOException, EmployeeException, LoginException;

	ResponseEntity<Object> verifyOtp(String verifyOtp, EmployeeRegister employee)
			throws IOException, SettingException, EmployeeException, LoginException;

}
