package com.wissen.servicecatalog.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.EmployeeRoleMaster;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.EmployeUpdateRequest;
import com.wissen.servicecatalog.pojo.EmployeeRequest;
import com.wissen.servicecatalog.pojo.EmployeeResponse;
import com.wissen.servicecatalog.pojo.ManagerResponse;
import com.wissen.servicecatalog.pojo.ResourceDetails;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.repository.EmployeeRoleMasterRepository;
import com.wissen.servicecatalog.repository.FeedbackRepository;
import com.wissen.servicecatalog.repository.ProjectRepository;
import com.wissen.servicecatalog.repository.ScoreRepository;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.repository.TowerSkillScoreRepository;
import com.wissen.servicecatalog.service.EmployeeService;
import com.wissen.servicecatalog.util.SendText;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass = true)
public class EmployeeServiceImpl implements EmployeeService {

	private static final String Q4 = "Q4";

	private static final String CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_CELL_SHOULD_NOT_BE_BLANK = "Cell should not be null/Cell type should be string/Cell should not be blank";

	private static final String PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID = "Please add the settings for Service catalog mail id";

	private static final String PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID = "Please add the settings for Service catalog password id";

	private static final String PUBLISHED = "Published";

	private static final String VERIFIED = "Verified";

	private static final String INVALID_EMPLOYEE_ID = "Invalid Employee id";

	private static final String PLEASE_ENTER_THE_EMPLOYEE_ID = "Please enter the employee id";

	private static final String NO_RECORDS_FOUND = "No Records found";

	private static final String EMAIL_ID_IS_ALREADY_REGISTERED = "Email ID is already registered";

	private static final String EMPLOYEE_ID_IS_ALREADY_REGISTERED = "Employee ID is already registered";

	private static final String MANAGER_ID_SHOULD_BE_4_DIGITS = "Manager Id should be 4 digits";

	private static final String EMPLOYEE_ID_SHOULD_BE_4_DIGITS = "Employee Id should be 4 digits";

	private static final String NOT_VERIFIED = "Not verified";

	Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Value("${regex}")
	String regex;

	@Autowired
	EmployeeMasterRepository employeeRepository;

	@Autowired
	EmployeeRepository employeeRRepository;

	@Autowired
	FeedbackRepository feedBackRepository;

	@Autowired
	EmployeeRoleMasterRepository employeeRoleMasterRepository;

	@Autowired
	ApplicationRoleMasterRepository applicationRoleMasterRepository;

	@Autowired
	ScoreRepository scoreRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	TowerRepository towerRepository;

	@Autowired
	TowerSkillScoreRepository towerSkillScoreRepository;

	@Autowired
	SettingRepository settingRepository;

	@Autowired
	LoginServiceImpl loginService;

	@Autowired
	SendText sendText;

	private XSSFWorkbook workbook;

	private XSSFSheet sheet;

	private List<EmployeeMaster> listEmployee;

	public EmployeeServiceImpl(List<EmployeeMaster> listEmployee) {
		this.listEmployee = listEmployee;
		workbook = new XSSFWorkbook();

	}

	@Override
	public EmployeeMaster getEmployeDetails(String email) throws EmployeeException {
		logger.info("Getting Employee Details from Employee Service");
		EmployeeMaster employee = employeeRepository.findByEmailId(email);
		if (employee == null) {
			logger.error("the Employee Does'nt have any data to show");
			throw new EmployeeException("the Employee Dose'nt have any data to show");
		}

		return employee;
	}

	@Override
	public EmployeeMaster addEmployee(EmployeeRequest employeeRequest) throws EmployeeException {
		logger.info("Adding Employee from Employee Service ");
		EmployeeMaster employeeId = employeeRepository.findByEmployeeId(employeeRequest.getEmployeeId());

		if (employeeId != null) {
			logger.error(EMPLOYEE_ID_IS_ALREADY_REGISTERED);
			throw new EmployeeException(EMPLOYEE_ID_IS_ALREADY_REGISTERED);
		}
		if (employeeRequest.getEmployeeId() < 999) {
			logger.error(EMPLOYEE_ID_SHOULD_BE_4_DIGITS);
			throw new EmployeeException(EMPLOYEE_ID_SHOULD_BE_4_DIGITS);
		}
		if (employeeRequest.getManagerId() < 999) {
			logger.error(MANAGER_ID_SHOULD_BE_4_DIGITS);
			throw new EmployeeException(MANAGER_ID_SHOULD_BE_4_DIGITS);
		}

		EmployeeMaster employeeMasterEmail = employeeRepository.findByEmailId(employeeRequest.getEmailId());
		if (employeeMasterEmail != null) {
			logger.error(EMAIL_ID_IS_ALREADY_REGISTERED);
			throw new EmployeeException(EMAIL_ID_IS_ALREADY_REGISTERED);
		}

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		ApplicationRoleMaster applicationRoleMaster = applicationRoleMasterRepository
				.findByApplicationName(employeeRequest.getApplicationRoleName());

		EmployeeRoleMaster employeeRoleMaster = employeeRoleMasterRepository
				.findByEmployeeRoleName(employeeRequest.getCurrentEmployeeRoleName());

		if (applicationRoleMaster == null && employeeRoleMaster == null) {
			return applicationRoleMasterNullAndEmployeeRoleMasterNull(employeeRequest, modelMapper);
		}

		if (applicationRoleMaster == null && employeeRoleMaster != null) {
			return applicationRoleMasterNull(employeeRequest, employeeRoleMaster, modelMapper);
		}

		if (applicationRoleMaster != null && employeeRoleMaster == null) {
			return employeeRoleMasterNull(employeeRequest, applicationRoleMaster, modelMapper);
		} else {
			EmployeeMaster employeeMasterDataPresent = modelMapper.map(employeeRequest, EmployeeMaster.class);
			employeeMasterDataPresent.setManagerId(employeeRequest.getManagerId());
			employeeMasterDataPresent.setApplicationRoleMaster(applicationRoleMaster);
			employeeMasterDataPresent.setCurrentEmployeeRoleId(employeeRoleMaster);
			employeeMasterDataPresent.setCertification(employeeRequest.getCertification());
			employeeMasterDataPresent.setEmployeeStatus(NOT_VERIFIED);
			return employeeRepository.save(employeeMasterDataPresent);
		}

	}

	public EmployeeMaster employeeRoleMasterNull(EmployeeRequest employeeRequest,
			ApplicationRoleMaster applicationRoleMaster, ModelMapper modelMapper) {

		EmployeeRoleMaster employeeRoleMasterNull = new EmployeeRoleMaster();
		employeeRoleMasterNull.setEmployeeRoleName(employeeRequest.getCurrentEmployeeRoleName());
		employeeRoleMasterRepository.save(employeeRoleMasterNull);
		EmployeeMaster employeeMasterEmployeeRoleNull = modelMapper.map(employeeRequest, EmployeeMaster.class);
		
		if (employeeRequest.getId() != null) {
			employeeMasterEmployeeRoleNull.setId(employeeRequest.getId());
		}
		employeeMasterEmployeeRoleNull.setManagerId(employeeRequest.getManagerId());
		employeeMasterEmployeeRoleNull.setApplicationRoleMaster(applicationRoleMaster);
		employeeMasterEmployeeRoleNull.setCurrentEmployeeRoleId(employeeRoleMasterNull);
		employeeMasterEmployeeRoleNull.setCertification(employeeRequest.getCertification());
		employeeMasterEmployeeRoleNull.setEmployeeStatus(NOT_VERIFIED);
		return employeeRepository.save(employeeMasterEmployeeRoleNull);

	}

	public EmployeeMaster applicationRoleMasterNull(EmployeeRequest employeeRequest,
			EmployeeRoleMaster employeeRoleMaster, ModelMapper modelMapper) {

		ApplicationRoleMaster aplicationRoleMasterNull = new ApplicationRoleMaster();
		aplicationRoleMasterNull.setApplicationName(employeeRequest.getApplicationRoleName());
		applicationRoleMasterRepository.save(aplicationRoleMasterNull);
		EmployeeMaster employeeMasterApplicationRoleNull = modelMapper.map(employeeRequest, EmployeeMaster.class);
		
		if (employeeRequest.getId() != null) {
			employeeMasterApplicationRoleNull.setId(employeeRequest.getId());
		}
		employeeMasterApplicationRoleNull.setManagerId(employeeRequest.getManagerId());
		employeeMasterApplicationRoleNull.setApplicationRoleMaster(aplicationRoleMasterNull);
		employeeMasterApplicationRoleNull.setCurrentEmployeeRoleId(employeeRoleMaster);
		employeeMasterApplicationRoleNull.setCertification(employeeRequest.getCertification());
		employeeMasterApplicationRoleNull.setEmployeeStatus(NOT_VERIFIED);
		return employeeRepository.save(employeeMasterApplicationRoleNull);
	}

	public EmployeeMaster applicationRoleMasterNullAndEmployeeRoleMasterNull(EmployeeRequest employeeRequest,
			ModelMapper modelMapper) {

		ApplicationRoleMaster applicationRoleMasterNull = new ApplicationRoleMaster();
		EmployeeRoleMaster employeeRoleMasterNull = new EmployeeRoleMaster();

		applicationRoleMasterNull.setApplicationName(employeeRequest.getApplicationRoleName());
		applicationRoleMasterRepository.save(applicationRoleMasterNull);
		employeeRoleMasterNull.setEmployeeRoleName(employeeRequest.getCurrentEmployeeRoleName());
		employeeRoleMasterRepository.save(employeeRoleMasterNull);

		EmployeeMaster employeeMaster = modelMapper.map(employeeRequest, EmployeeMaster.class);
		if (employeeRequest.getId() != null) {
			employeeMaster.setId(employeeRequest.getId());
		}
		employeeMaster.setApplicationRoleMaster(applicationRoleMasterNull);
		employeeMaster.setCurrentEmployeeRoleId(employeeRoleMasterNull);
		employeeMaster.setCertification(employeeRequest.getCertification());
		employeeMaster.setEmployeeStatus(NOT_VERIFIED);
		return employeeRepository.save(employeeMaster);
	}

	@Override
	public List<EmployeeResponse> getAllEmployee() throws EmployeeException {

		logger.info("Getting all Employees from Employee Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<EmployeeMaster> empolyeeMaster = employeeRepository.findAll();
		if (empolyeeMaster.isEmpty()) {
			logger.error(NO_RECORDS_FOUND);
			throw new EmployeeException(NO_RECORDS_FOUND);
		}
		List<EmployeeResponse> employeeResponseList = new ArrayList<>();
		for (EmployeeMaster empolyeeMasterList : empolyeeMaster) {
			EmployeeResponse employeeResponse = new EmployeeResponse();
			modelMapper.map(empolyeeMasterList, employeeResponse);
			employeeResponse.setApplicationRoleName(empolyeeMasterList.getApplicationRoleMaster().getApplicationName());
			employeeResponse
			.setCurrentEmployeeRoleName(empolyeeMasterList.getCurrentEmployeeRoleId().getEmployeeRoleName());
			employeeResponseList.add(employeeResponse);
		}
		return employeeResponseList;
	}

	@Override
	public EmployeeResponse getEmployee(Integer employeeId) throws EmployeeException {
		logger.info("Getting Employee from Employee Service");
		if (employeeId == null) {
			logger.error(PLEASE_ENTER_THE_EMPLOYEE_ID);
			throw new EmployeeException(PLEASE_ENTER_THE_EMPLOYEE_ID);

		}
		EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(employeeId);

		if (employeeMaster != null) {
			EmployeeResponse employeeResponse = new EmployeeResponse();

			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setAmbiguityIgnored(true);
			modelMapper.map(employeeMaster, employeeResponse);
			employeeResponse.setApplicationRoleName(employeeMaster.getApplicationRoleMaster().getApplicationName());
			employeeResponse
			.setCurrentEmployeeRoleName(employeeMaster.getCurrentEmployeeRoleId().getEmployeeRoleName());

			return employeeResponse;
		}
		logger.error(INVALID_EMPLOYEE_ID);
		throw new EmployeeException(INVALID_EMPLOYEE_ID);

	}

	@Override
	public List<EmployeeMaster> updateEmployee(List<EmployeeRequest> employeeRequestList) throws EmployeeException {
		logger.info("Updating Employee from Employee Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		List<EmployeeMaster> employeeResponseList = new ArrayList<>();
		for (EmployeeRequest employeeRequest : employeeRequestList) {
			EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(employeeRequest.getEmployeeId());
			if (employeeMaster == null) {
				logger.error(INVALID_EMPLOYEE_ID);
				throw new EmployeeException(INVALID_EMPLOYEE_ID);
			}

			if (employeeRequest.getEmployeeId() < 999) {
				logger.error(EMPLOYEE_ID_SHOULD_BE_4_DIGITS);
				throw new EmployeeException(EMPLOYEE_ID_SHOULD_BE_4_DIGITS);
			}
			if (employeeRequest.getManagerId() < 999) {
				throw new EmployeeException(MANAGER_ID_SHOULD_BE_4_DIGITS);
			}
			modelMapper.map(employeeRequest, employeeMaster);
			
			ApplicationRoleMaster applicationRoleMaster = applicationRoleMasterRepository
					.findByApplicationName(employeeRequest.getApplicationRoleName());

			EmployeeRoleMaster employeeRoleMaster = employeeRoleMasterRepository
					.findByEmployeeRoleName(employeeRequest.getCurrentEmployeeRoleName());

			if (applicationRoleMaster == null && employeeRoleMaster == null) {

				employeeResponseList.add(applicationRoleMasterNullAndEmployeeRoleMasterNull(employeeRequest, modelMapper));
			}

			if (applicationRoleMaster == null && employeeRoleMaster != null) {
				employeeResponseList.add(applicationRoleMasterNull(employeeRequest, employeeRoleMaster, modelMapper));
			}

			if (applicationRoleMaster != null && employeeRoleMaster == null) {
				employeeResponseList.add(employeeRoleMasterNull(employeeRequest, applicationRoleMaster, modelMapper));

			} else {
				if (employeeRequest.getId() != null) {
					employeeMaster.setId(employeeRequest.getId());
				}

				employeeMaster.setManagerId(employeeRequest.getManagerId());
				employeeMaster.setApplicationRoleMaster(applicationRoleMaster);
				employeeMaster.setCurrentEmployeeRoleId(employeeRoleMaster);
			}
			employeeResponseList.add(employeeRepository.save(employeeMaster));
		}
		if (employeeResponseList.isEmpty())
			throw new EmployeeException(NO_RECORDS_FOUND);

		return employeeResponseList;
	}

	@Override
	public String requestUpdate(Integer employeeId, String newName, String newMail)
			throws EmployeeException, MessagingException, IOException, SettingException {
		logger.info("Requesting Update from Employee Service");

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(newMail);
		if (!matcher.matches()) {
			throw new EmployeeException("Please enter the wissen mail id");
		}
		if (employeeId == null) {
			logger.error(PLEASE_ENTER_THE_EMPLOYEE_ID);
			throw new EmployeeException(PLEASE_ENTER_THE_EMPLOYEE_ID);
		}
		if (newName == null)
		{
			logger.error("Please enter the new name");
			throw new EmployeeException("Please enter the new name");
		}
		if (newMail == null)
		{
			logger.error("Invalid email");
			throw new EmployeeException("Invalid email id");
		}
		EmployeeMaster employee = employeeRepository.findByEmployeeId(employeeId);

		if (employee == null)
		{	
			logger.error(INVALID_EMPLOYEE_ID);
			throw new EmployeeException(INVALID_EMPLOYEE_ID);
		}
		String sub = "Request for changes";
		String msg = "<html>" + "<head>" + "<h2 style=font-family:'Lato Regular'; text-align:'center;'> Service Catalog"
				+ "</h2>" + "<body>" + "Hello ," + "<br>" + "Emp Id :" + employee.getEmployeeId() + "<br>" + "<br>"
				+ "The previous details are not correct ," + "Could you please change these details as below" + "<br>"
				+ "<br>" + "Name :" + newName + "<br>" + "New Mail:" + newMail + "<br>" + "Regards" + "<br>" + "Admin"
				+ "</head>" + "</body>" + "</html>";

		Setting setting = settingRepository.findBySettingName("HR Mail ID");
		if (setting == null)
			throw new SettingException("Please add the settings for service catalog hr mail id");
		Setting findBySettingMailId = settingRepository.findBySettingName("service-catalog mail id");
		if (findBySettingMailId == null)
			throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID);

		Setting findBySettingMailPassword = settingRepository.findBySettingName("service-catalog mail id password");

		if (findBySettingMailPassword == null)
			throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID);

		try {
			String mail[] = { setting.getData() };
			sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, sub, msg);
		} catch (Exception e) {
			logger.error("Failed To send email!,due to port issue", e);
			throw new EmployeeException("Failed to send email due to error : ".concat(e.getMessage()));
		}

		return "Message Sent sucessfully";
	}

	@Override
	public String updateByEmployee(EmployeUpdateRequest employee, Integer employeeId) throws EmployeeException {
		logger.info("Updating by Employee from Employee Service");
		if (employeeId == null) {
			logger.error(PLEASE_ENTER_THE_EMPLOYEE_ID);
			throw new EmployeeException(PLEASE_ENTER_THE_EMPLOYEE_ID);
		}
		EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(employeeId);
		if (employeeMaster == null) {
			logger.error(INVALID_EMPLOYEE_ID);
			throw new EmployeeException(INVALID_EMPLOYEE_ID);
		}
		EmployeeRoleMaster employeeRoleMaster = employeeRoleMasterRepository
				.findByEmployeeRoleName(employee.getCurrentEmployeeRoleName());

		employeeMaster.setTotalExperienceMonths(employee.getTotalExperienceMonths());
		employeeMaster.setCurrentRoleExperienceMonths(employee.getCurrentRoleExperienceMonths());

		if (employeeRoleMaster == null) {

			if (employee.isEmployeeStatus())
				employeeMaster.setEmployeeStatus(VERIFIED);
			else
				employeeMaster.setEmployeeStatus(NOT_VERIFIED);

			EmployeeRoleMaster employeeRoleMasterNull = new EmployeeRoleMaster();
			employeeRoleMasterNull.setEmployeeRoleName(employee.getCurrentEmployeeRoleName());

			employeeRoleMasterRepository.save(employeeRoleMasterNull);

			employeeMaster.setCurrentEmployeeRoleId(employeeRoleMasterNull);

			employeeMaster.setCertification(employee.getCertification());
			employeeMaster.setPrimarySkill(employee.getPrimarySkill());
			employeeMaster.setSecondarySkill(employee.getSecondarySkill());
			employeeMaster.setCurrentEmployeeRoleId(employeeRoleMaster);
			employeeRepository.save(employeeMaster);

		}
		if (!employee.isEmployeeStatus())
			employeeMaster.setEmployeeStatus(NOT_VERIFIED);
		else
			employeeMaster.setEmployeeStatus(VERIFIED);

		employeeMaster.setCurrentEmployeeRoleId(employeeRoleMaster);
		employeeMaster.setCertification(employee.getCertification());
		employeeMaster.setPrimarySkill(employee.getPrimarySkill());
		employeeMaster.setSecondarySkill(employee.getSecondarySkill());
		employeeRepository.save(employeeMaster);

		return "Updated Sucesfully";

	}

	@Override
	public List<ManagerResponse> getAllManager() throws EmployeeException {
		logger.info("Getting all Manager from Employee Service");

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		List<EmployeeMaster> employeeMaster = employeeRepository
				.findByApplicationRoleMasterApplicationName("Project Manager");

		if (employeeMaster.isEmpty()) {
			logger.error("No Project Manager's Details");
			throw new EmployeeException("No Project Manager's Details");

		}
		List<ManagerResponse> employeeResponseList = new ArrayList<>();
		for (EmployeeMaster empolyeeMasterList : employeeMaster) {
			ManagerResponse employeeResponse = new ManagerResponse();
			modelMapper.map(empolyeeMasterList, employeeResponse);
			employeeResponse.setProjectManagerName(empolyeeMasterList.getEmployeeName());
			employeeResponse.setApplicationRoleName(empolyeeMasterList.getApplicationRoleMaster().getApplicationName());
			employeeResponse
			.setCurrentEmployeeRoleName(empolyeeMasterList.getCurrentEmployeeRoleId().getEmployeeRoleName());
			employeeResponseList.add(employeeResponse);
		}
		return employeeResponseList;
	}

	@Override
	public ResponseEntity<Object> importEmployeeDetails(MultipartFile file)
			throws EmployeeException, MessagingException, SettingException, IOException {
		logger.info("Importing Employee Details from Employee Service");

		if (file.isEmpty()) {
			logger.error("Please upload the file");
			throw new EmployeeException("Please upload the file");
		}
		List<EmployeeMaster> list = new ArrayList<>();
		List<String> reject = new ArrayList<>();
		Workbook workbookFile = getWorkBook(file);
		if (workbookFile == null) {
			throw new EmployeeException("No workbook found");
		}
		Sheet sheetNumber = workbookFile.getSheetAt(0);
		if (sheetNumber == null) {
			throw new EmployeeException("No sheet found");
		}
		Iterator<Row> rows = sheetNumber.iterator();
		rows.next();
		rows.next();

		while (rows.hasNext()) {
			Row result = rows.next();
			Cell c = result.getCell(1);

			Cell cell = result.getCell(3);
			if (cell != null && cell.getCellTypeEnum() != CellType.NUMERIC
					&& cell.getCellTypeEnum() != CellType.BLANK) {
				throw new EmployeeException(
						"Total experience should not be null/Cell type should be numeric/Cell should not be blank");
			}

			cell = result.getCell(4);
			if (cell != null && cell.getCellTypeEnum() != CellType.NUMERIC
					&& cell.getCellTypeEnum() != CellType.BLANK) {
				throw new EmployeeException(
						"Current experience should not be null/Cell type should be numeric/Cell should not be blank");
			}
			cell = result.getCell(5);
			if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && cell.getCellTypeEnum() == CellType.STRING) {
				String managerId = result.getCell(5).getStringCellValue() + "";
				if (managerId.startsWith("WC") || managerId.equalsIgnoreCase("NA")) {
					reject.add(managerId + " is invalid in manager column");
					continue;
				}
			}
			cell = result.getCell(6);
			if (cell != null && cell.getCellTypeEnum() != CellType.STRING && cell.getCellTypeEnum() != CellType.BLANK) {
				throw new EmployeeException("Skill cannot be blank or null/Should be string/Should not be blank");
			}

			cell = result.getCell(7);
			if (cell != null && cell.getCellTypeEnum() != CellType.STRING && cell.getCellTypeEnum() != CellType.BLANK) {
				throw new EmployeeException(
						CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_CELL_SHOULD_NOT_BE_BLANK);
			}

			cell = result.getCell(8);
			if (cell != null && cell.getCellTypeEnum() != CellType.STRING && cell.getCellTypeEnum() != CellType.BLANK) {
				throw new EmployeeException(
						CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_CELL_SHOULD_NOT_BE_BLANK);
			}
			cell = result.getCell(9);
			if (cell == null || cell.getCellTypeEnum() != CellType.STRING || cell.getCellTypeEnum() == CellType.BLANK) {
				throw new EmployeeException(
						CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_CELL_SHOULD_NOT_BE_BLANK);
			}
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(result.getCell(9).getStringCellValue());
			if (!matcher.matches()) {

				reject.add(result.getCell(9).getStringCellValue() + " is invalid in email column");
				continue;
			}
			cell = result.getCell(10);
			if (cell != null && cell.getCellTypeEnum() != CellType.STRING && cell.getCellTypeEnum() != CellType.BLANK) {
				throw new EmployeeException(
						CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_CELL_SHOULD_NOT_BE_BLANK);
			}

			cell = result.getCell(11);
			if (cell == null || cell.getCellTypeEnum() != CellType.STRING || cell.getCellTypeEnum() == CellType.BLANK) {
				throw new EmployeeException(
						CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_CELL_SHOULD_NOT_BE_BLANK);
			}

			cell = result.getCell(12);
			if (cell == null || cell.getCellTypeEnum() != CellType.STRING || cell.getCellTypeEnum() == CellType.BLANK) {
				throw new EmployeeException(
						"Application role should not be null/Cell type should be string/Cell should not be blank");
			}
			ApplicationRoleMaster applicationRoleName = null;
			if (result.getCell(12).getStringCellValue().equalsIgnoreCase("Manager")) {
				String manager = "Project Manager";
				applicationRoleName = applicationRoleMasterRepository.findByApplicationName(manager);

			} else {
				applicationRoleName = applicationRoleMasterRepository
						.findByApplicationName(result.getCell(12).getStringCellValue());
			}
			EmployeeRoleMaster employeeRoleMaster = employeeRoleMasterRepository
					.findByEmployeeRoleName(result.getCell(11).getStringCellValue());

			EmployeeMaster employeeMaster = employeeRepository
					.findByEmployeeId((int) result.getCell(1).getNumericCellValue());
			if (employeeMaster == null && c != null) {

				if (applicationRoleName == null && employeeRoleMaster == null) {

					EmployeeMaster employee = new EmployeeMaster();

					employee = commonDetailsInImportFile(employee, result);
					ApplicationRoleMaster applicationRoleMasterNull = new ApplicationRoleMaster();

					applicationRoleMasterNull.setApplicationName(result.getCell(12).getStringCellValue());
					ApplicationRoleMaster applicationRoleMaster = applicationRoleMasterRepository
							.save(applicationRoleMasterNull);

					employee.setApplicationRoleMaster(applicationRoleMaster);

					EmployeeRoleMaster employeeRoleMasterNull = new EmployeeRoleMaster();

					employeeRoleMasterNull.setEmployeeRoleName(result.getCell(11).getStringCellValue());
					EmployeeRoleMaster employeeRole = employeeRoleMasterRepository.save(employeeRoleMasterNull);

					employee.setCurrentEmployeeRoleId(employeeRole);

					list.add(employee);
				}

				if (applicationRoleName == null && employeeRoleMaster != null) {
					EmployeeMaster employee = new EmployeeMaster();
					employee = commonDetailsInImportFile(employee, result);

					ApplicationRoleMaster applicationRoleMasterNull = new ApplicationRoleMaster();

					applicationRoleMasterNull.setApplicationName(result.getCell(12).getStringCellValue());
					ApplicationRoleMaster applicationRoleMaster = applicationRoleMasterRepository
							.save(applicationRoleMasterNull);

					employee.setApplicationRoleMaster(applicationRoleMaster);

					employee.setCurrentEmployeeRoleId(employeeRoleMaster);

					list.add(employee);
				}

				if (employeeRoleMaster == null && applicationRoleName != null) {

					EmployeeMaster employee = new EmployeeMaster();

					employee = commonDetailsInImportFile(employee, result);

					employee.setApplicationRoleMaster(applicationRoleName);
					EmployeeRoleMaster employeeRoleMasterNull = new EmployeeRoleMaster();

					employeeRoleMasterNull.setEmployeeRoleName(result.getCell(11).getStringCellValue());
					EmployeeRoleMaster employeeRoleMaster2 = employeeRoleMasterRepository.save(employeeRoleMasterNull);
					employee.setCurrentEmployeeRoleId(employeeRoleMaster2);

					list.add(employee);
				}
				if (employeeRoleMaster != null && applicationRoleName != null) {
					EmployeeMaster employee = new EmployeeMaster();

					EmployeeMaster duplicateEmployee = employeeRepository.findByDuplicateEmployee(
							(int) result.getCell(1).getNumericCellValue(), result.getCell(2).getStringCellValue(),
							(int) result.getCell(3).getNumericCellValue(),
							(int) result.getCell(4).getNumericCellValue(),
							(int) result.getCell(5).getNumericCellValue(), result.getCell(6).getStringCellValue(),
							result.getCell(7).getStringCellValue(), result.getCell(8).getStringCellValue(),
							result.getCell(9).getStringCellValue(), result.getCell(10).getStringCellValue(),
							employeeRoleMaster, applicationRoleName);

					if (duplicateEmployee != null) {
						continue;
					}
					employee = commonDetailsInImportFile(employee, result);

					employee.setApplicationRoleMaster(applicationRoleName);
					employee.setCurrentEmployeeRoleId(employeeRoleMaster);
					list.add(employee);
				}
			}
		}
		if (list.isEmpty()) {
			logger.error("Employee Details already existed");
			throw new EmployeeException("Employee Details already exist");
		}
		List<EmployeeMaster> saveAll2 = employeeRepository.saveAll(list);
		EmployeeServiceImpl emp = new EmployeeServiceImpl();
		List<Employee> collect = saveAll2.stream()
				.map(i -> new Employee(0, i.getEmployeeId() + "", i.getEmailId(), emp.autoGeneratePassword(),
						LocalDateTime.now().minusDays(92), i.getApplicationRoleMaster(), "Mail not sent", null))
				.collect(Collectors.toList());
		List<Employee> saveAll = employeeRRepository.saveAll(collect);
		sendRegisterationMail(saveAll);
		if (reject.isEmpty()) {
			return new ResponseEntity<>(reject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Employee data uploaded ", HttpStatus.OK);

	}

	@Async
	public void sendRegisterationMail(List<Employee> collect) throws SettingException, EmployeeException {
		BCryptPasswordEncoder encoderPassword = new BCryptPasswordEncoder();
		Setting findBySettingMailId = settingRepository.findBySettingName("service-catalog mail id");
		if (findBySettingMailId == null)
			throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);

		Setting findBySettingMailPassword = settingRepository.findBySettingName("service-catalog mail id password");

		if (findBySettingMailPassword == null)
			throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID);

		for (Employee employee : collect) {
			if (employee.getStatus().equalsIgnoreCase("Mail not sent")) {
				String sub = "User default password";
				String msg = "<html>" + "<head>"
						+ "<h2 style=font-family:'Lato Regular'; text-align:'center;'> Service Catalog" + "</h2>"
						+ "<body>" + "Hello Employee, " + "<br>" + "<br>"
						+ "Welcome to Service Catalog portal, Please enter this password " + "<b>"
						+ employee.getPassword() + "<b>" + "<br>" + " for your first login and reset the password,"
						+ "<br>" + "<br>" + "Please dont share your password with anyone! " + "<br>" + "<br>"
						+ "Regards" + "<br>" + "Admin" + "</body>" + "</html>";

				try {
					String mail[] = { employee.getEmail() };
					sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), mail, sub, msg);
					employee.setPassword(encoderPassword.encode(employee.getPassword()));
					employee.setStatus("Approved");
					employeeRRepository.save(employee);

				} catch (Exception e) {
					logger.error("Failed To send email!,due to port issue", e);
					throw new EmployeeException("Failed to send email due to error : ".concat(e.getMessage()));
				}

			}
		}
	}

	public EmployeeMaster commonDetailsInImportFile(EmployeeMaster employee, Row result) {

		employee.setEmployeeId((int) result.getCell(1).getNumericCellValue());
		employee.setEmailId(result.getCell(9).getStringCellValue());

		Cell cell = result.getCell(5);
		if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
			employee.setManagerId(employee.getEmployeeId());
		}
		employee.setManagerId((int) result.getCell(5).getNumericCellValue());
		employee.setEmployeeName(result.getCell(2).getStringCellValue());

		employee.setEmployeeName(result.getCell(2).getStringCellValue());
		cell = result.getCell(3);
		if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
			employee.setTotalExperienceMonths(0);
		} else {
			employee.setTotalExperienceMonths((int) result.getCell(3).getNumericCellValue());
		}
		cell = result.getCell(4);
		if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
			employee.setCurrentRoleExperienceMonths(0);

		} else {
			employee.setCurrentRoleExperienceMonths((int) result.getCell(4).getNumericCellValue());
		}
		cell = result.getCell(6);
		if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
			employee.setPrimarySkill(null);
		} else {
			employee.setPrimarySkill(result.getCell(6).getStringCellValue());
		}
		cell = result.getCell(7);
		if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
			employee.setSecondarySkill(null);
		} else {
			employee.setSecondarySkill(result.getCell(7).getStringCellValue());
		}
		cell = result.getCell(8);

		if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
			employee.setCertification("N/A");
		} else {
			employee.setCertification(result.getCell(8).getStringCellValue());
		}
		cell = result.getCell(10);
		if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
			employee.setEmployeeStatus(VERIFIED);
		} else {
			employee.setEmployeeStatus(result.getCell(10).getStringCellValue());

		}
		return employee;
	}

	public Workbook getWorkBook(MultipartFile file) throws IOException {
		logger.info("Getting Workbook from Employee Service");

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		try {
			if (extension.equalsIgnoreCase("xls"))
				return new HSSFWorkbook(file.getInputStream());

			else if (extension.equalsIgnoreCase("xlsx"))
				return new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			throw new IOException("Invalid excel extension");
		}
		return null;
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeHeaderLine() {

		sheet = workbook.createSheet("Employee_master_details");

		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(20);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		createCell(row, 0, "Employee Information", style);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		font.setFontHeightInPoints((short) (10));

		row = sheet.createRow(1);
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		createCell(row, 0, "Sl/no", style);
		createCell(row, 1, "Employee id", style);
		createCell(row, 2, "Employee Name", style);
		createCell(row, 3, "Total Experience", style);
		createCell(row, 4, "Current experience", style);
		createCell(row, 5, "Manager Id", style);
		createCell(row, 6, "Primary skill", style);
		createCell(row, 7, "Secondary skill", style);
		createCell(row, 8, "Certificate", style);
		createCell(row, 9, "Email Id", style);
		createCell(row, 10, "Employee status", style);
		createCell(row, 11, "Employee Role", style);
		createCell(row, 12, "Application Role", style);

	}

	private void writeDataLines() {
		int rowCount = 2;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		int serialNo = 0;

		for (EmployeeMaster stu : listEmployee) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, ++serialNo, style);
			createCell(row, columnCount++, stu.getEmployeeId(), style);
			createCell(row, columnCount++, stu.getEmployeeName(), style);
			createCell(row, columnCount++, stu.getTotalExperienceMonths(), style);
			createCell(row, columnCount++, stu.getCurrentRoleExperienceMonths(), style);
			createCell(row, columnCount++, stu.getManagerId(), style);
			createCell(row, columnCount++, stu.getPrimarySkill(), style);
			createCell(row, columnCount++, stu.getSecondarySkill(), style);
			createCell(row, columnCount++, stu.getCertification(), style);
			createCell(row, columnCount++, stu.getEmailId(), style);
			createCell(row, columnCount++, stu.getEmployeeStatus(), style);
			createCell(row, columnCount++, stu.getCurrentEmployeeRoleId().getEmployeeRoleName(), style);
			createCell(row, columnCount, stu.getApplicationRoleMaster().getApplicationName(), style);
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	public Set<ResourceDetails> getAllResourceDetails() throws EmployeeException {

		Set<ResourceDetails> resourceList = new HashSet<>();

		String quarter = " ";

		if (LocalDate.now().getMonthValue() >= 1 && LocalDate.now().getMonthValue() <= 3) {
			quarter = "Q1";
		} else if (LocalDate.now().getMonthValue() >= 4 && LocalDate.now().getMonthValue() <= 6) {
			quarter = "Q2";
		} else if (LocalDate.now().getMonthValue() >= 7 && LocalDate.now().getMonthValue() <= 9) {
			quarter = "Q3";
		} else if (LocalDate.now().getMonthValue() >= 10 && LocalDate.now().getMonthValue() <= 12) {
			quarter = Q4;
		}

		List<Score> presentQuarter = scoreRepository.findByQuarterAndYearAndStatus(quarter, LocalDate.now().getYear(),
				PUBLISHED);

		Map<Integer, List<Score>> map = presentQuarter.stream()
				.collect(Collectors.groupingBy(i -> i.getEmployeeMaster().getEmployeeId()));

		Set<Integer> keys = map.keySet();
		for (Integer s1 : keys) {
			for (Score iterate : map.get(s1)) {

				List<Score> towerScore1 = scoreRepository.findByEmployeeIdAndTowerAndProject(s1,
						iterate.getTower().getTowerId(), iterate.getProject().getProjectId(), quarter);
				long currentQuarter = towerScore1.stream().collect(Collectors.summarizingInt(Score::getScore)).getSum();

				ResourceDetails resource = new ResourceDetails();
				resource.setEmployeeId(towerScore1.get(0).getEmployeeMaster().getEmployeeId());
				resource.setEmployeeName(towerScore1.get(0).getEmployeeMaster().getEmployeeName());
				resource.setTowerName(towerScore1.get(0).getTower().getTowerName());
				resource.setSkillLevel(towerScore1.get(0).getProjectSkill().getSkillLevel());
				resource.setProjectName(towerScore1.get(0).getProject().getProjectName());
				resource.setPrimarySkill(towerScore1.get(0).getEmployeeMaster().getPrimarySkill());
				resource.setSecondarySkill(towerScore1.get(0).getEmployeeMaster().getSecondarySkill());

				Project project = projectRepository.findByProjectNameAndTowerTowerId(
						towerScore1.get(0).getProject().getProjectName(), towerScore1.get(0).getTower().getTowerId());

				resource.setProjectManagerName(project.getProjectManagerId().getEmployeeName());

				TowerScoreSkillDetails findByTowerScore = towerSkillScoreRepository.findByTowerSkill(
						towerScore1.get(0).getTower().getTowerId(),
						towerScore1.get(0).getProjectSkill().getSkillLevel());
				resource.setMinScore(findByTowerScore.getMinimumScore());
				resource.setMaxScore(findByTowerScore.getMaximumScore());
				resource.setEmployeeStatus(towerScore1.get(0).getCurrentEmployeeStatus().getCurrentEmployeeStatus());
				resource.setRoadMap(towerScore1.get(0).getRoadMap());
				resource.setFeedbackName(towerScore1.get(0).getFeedbackMaster().getFeedbackName());

				resource.setCurrentQuarter((int) currentQuarter);
				String number = quarter.substring(1);

				Integer quarterNumber = Integer.parseInt(number) - 1;
				if (quarterNumber == 0)
					number = Q4;
				else
					number = "Q" + quarterNumber;

				long previousQuarterSum = 0;
				if (number.equalsIgnoreCase(Q4)) {
					List<Score> previousQuarterList = scoreRepository
							.findByQuarterAndStatusEmployeeIdAndYearTowerAndProject(number, PUBLISHED,
									towerScore1.get(0).getEmployeeMaster().getEmployeeId(),
									LocalDate.now().getYear() - 1, towerScore1.get(0).getTower().getTowerId(),
									towerScore1.get(0).getProject().getProjectId());
					previousQuarterSum = previousQuarterList.stream()
							.collect(Collectors.summarizingInt(Score::getScore)).getSum();
					resource.setPreviousQuarter((int) previousQuarterSum);
				} else {
					List<Score> previousQuarterList = scoreRepository
							.findByQuarterAndStatusEmployeeIdAndYearTowerAndProject(number, PUBLISHED,
									towerScore1.get(0).getEmployeeMaster().getEmployeeId(), LocalDate.now().getYear(),
									towerScore1.get(0).getTower().getTowerId(),
									towerScore1.get(0).getProject().getProjectId());
					previousQuarterSum = previousQuarterList.stream()
							.collect(Collectors.summarizingInt(Score::getScore)).getSum();
					resource.setPreviousQuarter((int) previousQuarterSum);

				}
				long sum = currentQuarter - previousQuarterSum;
				if (currentQuarter > previousQuarterSum) {
					resource.setQuarterComparsion("Improved");
				} else if (currentQuarter < previousQuarterSum)
					resource.setQuarterComparsion("Declined");
				else if (sum == 0) {
					resource.setQuarterComparsion("Status Quo");
				}
				resourceList.add(resource);
				continue;
			}
		}
		if (resourceList.isEmpty())
			throw new EmployeeException(NO_RECORDS_FOUND);
		return resourceList;
	}

	public String autoGeneratePassword() {
		return RandomStringUtils.randomAlphanumeric(16);
	}
}