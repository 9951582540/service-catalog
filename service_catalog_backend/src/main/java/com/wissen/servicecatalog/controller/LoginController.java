package com.wissen.servicecatalog.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Random;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.EmployeeRegister;
import com.wissen.servicecatalog.pojo.EmployeeRegisterRequest;
import com.wissen.servicecatalog.pojo.LoginRequest;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.service.impl.LoginServiceImpl;
import com.wissen.servicecatalog.util.JwtUtil;

import io.swagger.annotations.Api;

@Api(tags = "Login Service")
@RestController
@RequestMapping("/service-catalog/employee")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	@Qualifier("authenticationManager")
	AuthenticationManager authenticationManager;

	@Autowired
	LoginServiceImpl loginService;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserDetailsService userDetailsService;

	Random random = new Random();

	@PostMapping("/signup")
	public Employee userRegister(@RequestBody @Valid EmployeeRegisterRequest user)
			throws EmployeeException, IOException, SettingException, LoginException {
		logger.info("Sign up from Login Controller ");
		return loginService.userRegister(user);
	}

	@PostMapping(value = "/verifyOtp")
	public ResponseEntity<Object> verifyOtp(String verifyOtp, @RequestBody @Valid EmployeeRegister employee)
			throws EmployeeException, IOException, SettingException, LoginException {
		logger.info("Verify OTP from Login Controller");
		return loginService.verifyOtp(verifyOtp, employee);
	}

	private void authenticate(String username, String password) throws EmployeeException {
		logger.info("authenticating username and password from Login Controller");
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception e) {
			logger.error("INVALID_CREDENTIALS", e);
			throw new EmployeeException("INVALID_CREDENTIALS", e);
		}
	}

	@PostMapping("/signin")
	public String authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws EmployeeException {
		logger.info("Authenticating user before signing in from Login Controller");

		if (loginRequest.getEmployeeId().length() < 3) {
			logger.error("Employee Id should be 4 digits");
			throw new EmployeeException("Employee Id should be 4 digits");
		}
		UserDetails userDetails = null;
		try {
			authenticate(loginRequest.getEmployeeId(), loginRequest.getPassword());
			userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmployeeId());
		} catch (Exception e) {
			logger.error("Sign in from Login Controller");
			throw new EmployeeException("Invalid Employee Id & Password");
		}

		Employee employee = employeeRepository.findByEmployeeId(loginRequest.getEmployeeId() + "");

		if (employee == null) {
			logger.error("Invalid Employee Id");
			throw new EmployeeException("Invalid Employee Id");
		}
		if (employee.getStatus().equalsIgnoreCase("OTP not verified")) {
			logger.error("Your email id is not verified");
			throw new EmployeeException("Your email id is not verifed!");
		} else if (employee.getStatus().equalsIgnoreCase("Pending")) {
			logger.error("Registration is still pending, Please contact Admin");
			throw new EmployeeException("Registration is still pending, Please contact Admin");
		} else if (employee.getStatus().equalsIgnoreCase("Rejected")) {
			logger.error("Registration is rejected, Please contact Admin");
			throw new EmployeeException("Registration is rejected, Please contact Admin");
		}
		LocalDateTime loginPresentDateTime = LocalDateTime.now();
		LocalDateTime registerationDateTime = employee.getLocalDateTime();
		if (registerationDateTime.isBefore(loginPresentDateTime)) {
			LocalDate loginPresentDate = LocalDate.of(loginPresentDateTime.getYear(), loginPresentDateTime.getMonth(),
					loginPresentDateTime.getDayOfMonth());
			LocalDate registerationDate = LocalDate.of(registerationDateTime.getYear(),
					registerationDateTime.getMonth(), registerationDateTime.getDayOfMonth());
			Period difference = Period.between(registerationDate, loginPresentDate);
			if (difference.getMonths() >= 3) {
				logger.error("Reset the password");
				throw new EmployeeException("Reset the password");
			}
		}

		return jwtUtil.generateToken(userDetails);

	}

	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, @RequestParam("email") String email)
			throws IOException, EmployeeException, SettingException, LoginException {
		logger.info("Forgot Password from Login Controller");
		String email1 = request.getParameter("email");

		String token = String.format("%04d", random.nextInt(10000));


		loginService.updateResetPasswordToken(token, email1);

		loginService.sendEmailtoRegisteredEmployee(email1, token);

		return "OTP sent successfully";
	}

	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, @RequestParam("OtpNumber") String otpNumber,
			@RequestParam("Password") String password) throws EmployeeException {
		logger.info("Reset password from Login Controller");
		Employee customer = loginService.getByResetPasswordToken(otpNumber);
		if (customer == null) {
			return "Token Not verified";
		} else {
			loginService.updatePassword(customer, password);
		}

		return "Password changed successfully";
	}

	@PutMapping("/change-password/{employeeId}/{password}")
	public String changePassword(@PathVariable Integer employeeId, @PathVariable String password)
			throws EmployeeException {
		logger.info("Changing password by entering employee Id from Login Controller");
		return loginService.resetPassword(employeeId, password);
	}

	@GetMapping("/resend-otp/{email}")
	public String resendOTP(@PathVariable("email") String email) throws EmployeeException, SettingException, LoginException {
		return loginService.resendOTP(email);
	}
}
