package com.wissen.servicecatalog.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.OtpDetails;
import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.ApiResponse;
import com.wissen.servicecatalog.pojo.EmployeeRegister;
import com.wissen.servicecatalog.pojo.EmployeeRegisterRequest;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.repository.OtpRepository;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.service.LoginService;
import com.wissen.servicecatalog.util.SendText;

@Service
public class LoginServiceImpl implements LoginService {

    private static final String FAILED_TO_SEND_EMAIL_DUE_TO_ERROR = "Failed to send email due to error : ";
    private static final String FAILED_TO_SEND_THE_EMAIL_DUE_TO_PORT_ISSUE = "Failed To send the email !,due to port issue";
    private static final String ADMIN = "Admin";
    private static final String PLEASE_ENTER_THE_WISSEN_EMAIL_ID = "Please enter the wissen mail id";
    @Value("${regex}")
    String regex;
    private static final String HTML_OTP = "<html><head><body style='font-family:Lato Regular, Arial, Helvetica'><h3>Service Catalog</h3><body>Hello,<br/><br/>OTP for registration is : <b>%s</b><br/><br/>Regards<br>Admin</head></body></html>";
    private static final String USER_REGISTRATION = "User Registration";
    private static final String OTP_NOT_VERIFIED = "OTP not verified";
    private static final String PROJECT_MANAGER = "Project Manager";
    private static final String SERVICE_CATALOG_MAIL_ID = "service-catalog mail id";
    private static final String SERVICE_CATALOG_MAIL_ID_PASSWORD = "service-catalog mail id password";
    private static final String PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID = "Please add the settings for Service catalog mail id";
    private static final String PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID = "Please add the settings for Service catalog password id";
    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMasterRepository employeeMasterRepository;

    @Autowired
    ApplicationRoleMasterRepository applicationRoleRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    SettingRepository settingRepository;

    @Autowired
    SendText sendText;

    BCryptPasswordEncoder encoderPassword = new BCryptPasswordEncoder();

    Random random = new Random();

    @Override
    public ResponseEntity<Object> sendEmailtoUser(String email) throws EmployeeException, IOException {
	logger.info("Sending Email to User from Login Service");

	return ResponseEntity.ok(new ApiResponse(true, "successfully sent  otp"));
    }

    @Override
    public Employee userRegister(@RequestBody EmployeeRegisterRequest employee)
	    throws EmployeeException, IOException, SettingException, LoginException {

	Setting findBySettingMailId = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID);
	if (findBySettingMailId == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID);

	Setting findBySettingMailPassword = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID_PASSWORD);

	if (findBySettingMailPassword == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

	logger.info("User Registration fom Login Service");
	ModelMapper modelMap = new ModelMapper();
	modelMap.getConfiguration().setAmbiguityIgnored(true);

	EmployeeMaster employeeMaster = employeeMasterRepository
		.findByEmployeeId(Integer.parseInt(employee.getEmployeeId()));
	if (employeeMaster == null) {
	    logger.error("Unauthorized Employee registration, Please enter the wissen Employee ID to registration");
	    throw new EmployeeException(
		    "Unauthorized Employee registration, Please enter the wissen Employee ID to registration");
	}
	Employee employeeId = employeeRepository.findByEmployeeId(employeeMaster.getEmployeeId() + "");
	if (employeeId != null && employeeId.getStatus().equalsIgnoreCase("Mail not sent")) {
	    throw new EmployeeException("your default password is expired,please reset the password!");
	}
	if (employee.getApplicationRole().equalsIgnoreCase("Manager")) {
	    employee.setApplicationRole(PROJECT_MANAGER);
	}
	if (employee.getEmployeeId().length() < 3) {
	    logger.error("Employee Id should be 4 digits");
	    throw new EmployeeException("Employee Id should be 4 digits");
	}
	if (employeeRepository.findByEmail(employee.getEmail()) != null) {
	    logger.error("Email id is already registered");
	    throw new EmployeeException("Email id is already registered");
	}
	if (employeeRepository.findByEmployeeId(employee.getEmployeeId()) != null) {
	    logger.error("Employee id is already Exist");
	    throw new EmployeeException("Employee id is already Exist");
	}
	if (!employeeMaster.getEmailId().equalsIgnoreCase(employee.getEmail())) {
	    logger.error("Invalid email id,Please enter your wissen employee id");
	    throw new EmployeeException("Invalid email id,Please enter your wissen employee id");
	}
	if (!employeeMaster.getApplicationRoleMaster().getApplicationName()
		.equalsIgnoreCase(employee.getApplicationRole())) {
	    logger.error("Invalid Role,Please select your wissen Application role");
	    throw new EmployeeException("Invalid Role,Please select your wissen Application role");
	}

	Employee employee1 = new Employee();
	modelMap.map(employee, employee1);
	employee1.setLocalDateTime(LocalDateTime.now());
	employee1.setStatus(OTP_NOT_VERIFIED);
	employee1.setPassword(encoderPassword.encode(employee.getPassword()));

	ApplicationRoleMaster applicationRoleMasterCheck = applicationRoleRepository
		.findByApplicationName(employee.getApplicationRole());

	if (applicationRoleMasterCheck == null) {
	    ApplicationRoleMaster applicationRole = new ApplicationRoleMaster();
	    applicationRole.setApplicationName(employee.getApplicationRole());
	    ApplicationRoleMaster applicationRoleMaster = applicationRoleRepository.save(applicationRole);
	    employee1.setApplicationRole(applicationRoleMaster);
	    return sendMailForUserRegieration(employee1, findBySettingMailId, findBySettingMailPassword);
	}

	employee1.setApplicationRole(applicationRoleMasterCheck);
	return sendMailForUserRegieration(employee1, findBySettingMailId, findBySettingMailPassword);
    }

    public Employee sendMailForUserRegieration(Employee employee, Setting findBySettingMailId,
	    Setting findBySettingMailPassword) throws LoginException {
	String freshCode = String.format("%04d", random.nextInt(10000));
	OtpDetails otp = otpRepository.findByEmail(employee.getEmail());

	if (otp != null) {
	    otpRepository.delete(otp);
	}
	OtpDetails otpdetails = new OtpDetails();
	otpdetails.setEmail(employee.getEmail());

	otpdetails.setOtpNumber(freshCode);
	otpdetails.setValidFrom(LocalDateTime.now());
	otpdetails.setValidUpto(LocalDateTime.now().plusMinutes(5));
	otpRepository.save(otpdetails);
	try {
	    String[] mail = { employee.getEmail() };
	    sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, USER_REGISTRATION,
		    String.format(HTML_OTP, freshCode));

	    return employee;
	} catch (Exception e) {
	    logger.error(FAILED_TO_SEND_THE_EMAIL_DUE_TO_PORT_ISSUE, e);
	    throw new LoginException(FAILED_TO_SEND_EMAIL_DUE_TO_ERROR.concat(e.getMessage()));
	}

    }

    @Override
    public ResponseEntity<Object> verifyOtp(String verifyOtp, EmployeeRegister employee)
	    throws EmployeeException, IOException, SettingException, LoginException {
	logger.info("Verifying Otp from Login Service");

	Employee employeeSave = new Employee();
	ModelMapper modelMap = new ModelMapper();
	modelMap.getConfiguration().setAmbiguityIgnored(true);
	modelMap.map(employee, employeeSave);

	Setting findBySettingMailId = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID);
	if (findBySettingMailId == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

	Setting findBySettingMailPassword = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID_PASSWORD);

	if (findBySettingMailPassword == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

	if (verifyOtp != null) {
	    OtpDetails otp = otpRepository.findByOtp(verifyOtp);
	    if (otp == null) {
		return ResponseEntity.ok(new ApiResponse(false, "Otp invalid"));
	    }
	    Employee findByEmployeeId = employeeRepository.findByEmployeeId(employeeSave.getEmployeeId());
	    if (findByEmployeeId != null)
		return ResponseEntity.ok(new ApiResponse(false, "User is already registered"));

	    if (LocalDateTime.now().isBefore(otp.getValidUpto())) {
		if (employeeSave.getStatus().equalsIgnoreCase(OTP_NOT_VERIFIED)
			&& (employeeSave.getApplicationRole().getApplicationName().equalsIgnoreCase(PROJECT_MANAGER))
			|| employeeSave.getApplicationRole().getApplicationName().equalsIgnoreCase("Leadership")
			|| employeeSave.getApplicationRole().getApplicationName().equalsIgnoreCase("Senior Manager")
			|| employeeSave.getApplicationRole().getApplicationName().equalsIgnoreCase(ADMIN)) {
		    employeeSave.setStatus("Pending");
		    Setting adminMail = settingRepository.findBySettingName("Admin Mail id");
		    Setting adminMail2 = settingRepository.findBySettingName("Sub Admin Mail id");

		    if (adminMail == null) {
			throw new SettingException("Please add the Admin Mail id in settings");
		    }
		    String sub = "Employee Registration ";
		    String msg = "<html>" + "<head>"
			    + "<h2 style=font-family:'Lato Regular'; text-align:'center;'> Service Catalog" + "</h2>"
			    + "<body>" + "Hello Team," + "<br>"
			    + "I have Registered to Service Catolog Portal & my emp id is: "
			    + employeeSave.getEmployeeId() + "<br>" + "Please Approve My Request " + "<br>" + "<br>"
			    + "Regards" + "<br>" + ADMIN + "</head>" + "</body>" + "</html>";
		    try {
			if (adminMail2 == null) {
			    String[] mail = { adminMail.getData() };
			    sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, sub,
				    msg);
			} else {
			    String[] mail = { adminMail.getData(), adminMail.getData() };
			    sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, sub,
				    msg);
			}

			employeeRepository.save(employeeSave);
			return ResponseEntity.ok(new ApiResponse(true, "Valid Otp"));
		    } catch (Exception e) {
			logger.error(FAILED_TO_SEND_THE_EMAIL_DUE_TO_PORT_ISSUE, e);
			throw new LoginException(FAILED_TO_SEND_EMAIL_DUE_TO_ERROR.concat(e.getMessage()));
		    }

		}
		if (employeeSave.getStatus().equalsIgnoreCase(OTP_NOT_VERIFIED)
			&& employeeSave.getApplicationRole().getApplicationName().equalsIgnoreCase("Employee")) {
		    employeeSave.setStatus("Approved");
		    employeeRepository.save(employeeSave);
		    return ResponseEntity.ok(new ApiResponse(true, "Valid Otp"));
		}
	    }
	}
	return ResponseEntity.ok(new ApiResponse(false, "Please enter the otp"));

    }

    public void updateResetPasswordToken(String token, String email) throws EmployeeException {
	logger.info("Updating Reset Password Token from Login Service");

	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(email);
	if (!matcher.matches()) {
	    throw new EmployeeException(PLEASE_ENTER_THE_WISSEN_EMAIL_ID);
	}
	Employee customer = employeeRepository.findByEmail(email);
	if (customer != null) {
	    customer.setResetPasswordToken(token);
	    employeeRepository.save(customer);
	} else {
	    logger.error("Email id is not registered!");
	    throw new EmployeeException("Email id is not registered!");
	}
    }

    public Employee getByResetPasswordToken(String token) throws EmployeeException {
	logger.info("Getting by Reset Passwork Token from Login Service");
	Employee jwtUser = employeeRepository.findByResetPasswordToken(token);
	if (jwtUser == null) {
	    logger.error("Invalid otp,Please enter the valid otp!");
	    throw new EmployeeException("Invalid otp,Please enter the valid otp!");
	}
	jwtUser.setLocalDateTime(LocalDateTime.now());
	return employeeRepository.save(jwtUser);
    }

    public void updatePassword(Employee employee, String newPassword) {
	logger.info("Updating Password from Login Service");
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	String encodedPassword = passwordEncoder.encode(newPassword);
	employee.setPassword(encodedPassword);
	employee.setLocalDateTime(LocalDateTime.now());
	employeeRepository.save(employee);
    }

    @Override
    public ResponseEntity<Object> sendEmailtoRegisteredEmployee(String email, String link)
	    throws EmployeeException, IOException, SettingException, LoginException {

	logger.info("Sending Email to Registered Employee from Login Service");

	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(email);
	if (!matcher.matches()) {

	    throw new EmployeeException(PLEASE_ENTER_THE_WISSEN_EMAIL_ID);
	}

	Setting findBySettingMailId = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID);
	if (findBySettingMailId == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

	Setting findBySettingMailPassword = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID_PASSWORD);

	if (findBySettingMailPassword == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

	String sub = "Employee Registration ";
	String msg = "<html>" + "<head>" + "<h2 style=font-family:'Lato Regular'; text-align:'center;'> Service Catalog"
		+ "</h2>" + "<body>" + "Hello " + "<br>" + "Reset Password OTP:-" + link + "<br>" + "Regards" + "<br>"
		+ ADMIN + "</head>" + "</body>" + "</html>";

	try {
	    String mail[] = { email };
	    sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, sub, msg);
	} catch (Exception e) {
	    logger.error(FAILED_TO_SEND_THE_EMAIL_DUE_TO_PORT_ISSUE, e);
	    throw new LoginException(FAILED_TO_SEND_EMAIL_DUE_TO_ERROR.concat(e.getMessage()));
	}

	return ResponseEntity.ok(new ApiResponse(true, "successfully sent  otp"));
    }

    public String resetPassword(Integer employeeId, String password) throws EmployeeException {
	logger.info("Reseting Password from Login Service");
	Employee employee = employeeRepository.findByEmployeeId(employeeId + "");

	if (employee == null) {
	    logger.error("Invalid Employee id");
	    throw new EmployeeException("Invalid Employee id");
	}
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	if (!encoder.matches(password, employee.getPassword())) {
	    String pass = encoder.encode(password);
	    employee.setPassword(pass);
	    employee.setLocalDateTime(LocalDateTime.now());
	    employeeRepository.save(employee);
	    return "password changed";
	} else {
	    logger.error("please enter new password");
	    throw new EmployeeException("please enter new password");
	}
    }

    public String resendOTP(String email) throws EmployeeException, SettingException, LoginException {
	Setting findBySettingMailId = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID);

	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(email);
	if (!matcher.matches()) {

	    throw new EmployeeException(PLEASE_ENTER_THE_WISSEN_EMAIL_ID);
	}

	if (findBySettingMailId == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

	Setting findBySettingMailPassword = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID_PASSWORD);

	if (findBySettingMailPassword == null)
	    throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

	String freshCode = String.format("%04d", random.nextInt(10000));
	OtpDetails otp = otpRepository.findByEmail(email);

	if (otp == null) {

	    OtpDetails otpdetails = new OtpDetails();
	    otpdetails.setEmail(email);
	    otpdetails.setOtpNumber(freshCode);
	    otpdetails.setValidFrom(LocalDateTime.now());
	    otpdetails.setValidUpto(LocalDateTime.now().plusMinutes(5));
	    otpRepository.save(otpdetails);
	    return resendSendMail(email, findBySettingMailId, findBySettingMailPassword, freshCode);

	} else {
	    otp.setOtpNumber(freshCode);
	    otp.setValidFrom(LocalDateTime.now());
	    otp.setValidUpto(LocalDateTime.now().plusMinutes(5));
	    otpRepository.save(otp);
	    return resendSendMail(email, findBySettingMailId, findBySettingMailPassword, freshCode);
	}

    }

    public String resendSendMail(String email, Setting findBySettingMailId, Setting findBySettingMailPassword,
	    String freshCode) throws EmployeeException, LoginException {

	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(email);
	if (!matcher.matches()) {
	    throw new EmployeeException(PLEASE_ENTER_THE_WISSEN_EMAIL_ID);
	}

	String sub = USER_REGISTRATION;
	String msg = "<html>" + "<head>" + "<h2 style=font-family:'Lato Regular'; text-align:'center;'> Service Catalog"
		+ "</h2>" + "<body>" + "Hello ," + "<br>" + "OTP for registration is : " + freshCode + "<br>" + "<br>"
		+ "Regards" + "<br>" + ADMIN + "</head>" + "</body>" + "</html>";
	try {
	    String mail[] = { email };
	    sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, sub, msg);
	    return "otp sent sucessfully";
	} catch (Exception e) {
	    logger.error(FAILED_TO_SEND_THE_EMAIL_DUE_TO_PORT_ISSUE, e);
	    throw new LoginException(FAILED_TO_SEND_EMAIL_DUE_TO_ERROR.concat(e.getMessage()));
	}

    }
}