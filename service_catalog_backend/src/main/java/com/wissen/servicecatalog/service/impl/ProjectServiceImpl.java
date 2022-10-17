package com.wissen.servicecatalog.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ProjectRequest;
import com.wissen.servicecatalog.pojo.ProjectResponse;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.ProjectRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	private static final String PLEASE_UPLOAD_THE_FILE = "Please upload the file";
	private static final String PROJECT_NAME_IS_ALREADY_EXIST = "Project name is already exist";
	private static final String PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER = "please check the employee id, not an project manager";
	private static final String INVALID_EMPLOYEE_ID = "Invalid employee id";
	private static final String INVALID_TOWER_ID = "Invalid Tower Id";
	Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	EmployeeMasterRepository employeeRepository;

	@Autowired
	TowerRepository towerRepository;

	@Override
	public String addProject(ProjectRequest projectRequest) throws ProjectException, EmployeeException, TowerException {
		logger.info("Adding Project from Project Service");
		LinkedList<Tower> tower = new LinkedList<>();
		for (Integer list1 : projectRequest.getTowerId()) {

			Tower towerId = towerRepository.findByTowerId(list1);

			if (towerId == null)
				throw new TowerException(INVALID_TOWER_ID);

			tower.add(towerId);
		}
		EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(projectRequest.getProjectManagerId());

		if (employeeMaster == null) {
			logger.error(INVALID_EMPLOYEE_ID);
			throw new EmployeeException(INVALID_EMPLOYEE_ID);
		}

		if (!employeeMaster.getApplicationRoleMaster().getApplicationName().equalsIgnoreCase("Project Manager")) {
			logger.error(PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER);
			throw new EmployeeException(PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER);
		}

		for (Tower list : tower) {
			Project projectName = projectRepository.findByProjectManagerIdAndProjectNameAndTowerTowerId(
					projectRequest.getProjectManagerId(), projectRequest.getProjectName(), list.getTowerId());
			if (projectName != null) {
				logger.error(PROJECT_NAME_IS_ALREADY_EXIST);
				throw new ProjectException("Project name is already exist!");
			}
			Project project = new Project();
			project.setProjectName(projectRequest.getProjectName());
			project.setProjectManagerId(employeeMaster);
			project.setTower(list);
			projectRepository.save(project);
		}

		return "Project added";

	}

	@Override
	public ProjectResponse getProject(String projectName) throws ProjectException {

		logger.info("Getting Project from Project Service");
		if (projectName == null) {
			logger.error("Please enter the project id");
			throw new ProjectException("Please enter the project id");
		}

		List<Project> project = projectRepository.findByProjectName(projectName);
		if (project.isEmpty()) {
			logger.error("Invalid project Name");
			throw new ProjectException("Invalid project Name");

		}

		List<Tower> collect = project.stream().map(Project::getTower).collect(Collectors.toList());
		if (collect.isEmpty())
			throw new ProjectException("No towers for this project");

		ProjectResponse projectResponse = new ProjectResponse();
		projectResponse.setProjectId(project.get(0).getProjectId());
		projectResponse.setProjectManagerId(project.get(0).getProjectManagerId().getEmployeeId());
		projectResponse.setProjectManagerName(project.get(0).getProjectManagerId().getEmployeeName());
		projectResponse.setProjectName(project.get(0).getProjectName());
		projectResponse.setTower(collect);

		return projectResponse;
	}

	@Override
	public Set<ProjectResponse> getAllProject() throws ProjectException {
		logger.info("Getting all Project from Project Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<Project> projectList = projectRepository.findAll();
		if (projectList.isEmpty()) {
			logger.error("No projects found");
			throw new ProjectException("no projects found");
		}
		Set<ProjectResponse> projectResponseList = new LinkedHashSet<>();
		for (Project projectListResult : projectList) {
			List<Project> findByProjectName = projectRepository.findByProjectManagerIdAndProjectName(
					projectListResult.getProjectManagerId().getEmployeeId(), projectListResult.getProjectName());
			List<Tower> collect = findByProjectName.stream().map(Project::getTower).collect(Collectors.toList());
			ProjectResponse projectResponse = new ProjectResponse();
			projectResponse.setProjectId(findByProjectName.get(0).getProjectId());
			projectResponse.setProjectManagerId(projectListResult.getProjectManagerId().getEmployeeId());
			projectResponse.setProjectManagerName(projectListResult.getProjectManagerId().getEmployeeName());
			projectResponse.setProjectName(projectListResult.getProjectName());
			projectResponse.setTower(collect);
			projectResponseList.add(projectResponse);
		}
		return projectResponseList.stream().sorted((i, j) -> i.getProjectName().compareTo(j.getProjectName()))
				.collect(Collectors.toSet());
	}

	@Override
	public String updateProject(String projectName, Integer managerId)
			throws ProjectException, EmployeeException, TowerException {

		logger.info("Updating Project from Project Service");
		if (projectName == null) {
			logger.error("Please enter the project name");
			throw new ProjectException("Please enter the project name");

		}

		if (managerId == null) {
			logger.error("Please enter manager id");
			throw new ProjectException("Please enter manager id");
		}

		EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(managerId);
		if (employeeMaster == null) {
			logger.error(PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER);
			throw new EmployeeException("please check the employee id, not an project manager!");
		}
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<Project> project = projectRepository.findByProjectName(projectName);

		if (project.isEmpty()) {
			logger.error("Invalid project name");
			throw new ProjectException("Invalid project name");
		}
		List<Project> list = new LinkedList<>();
		for (Project proj : project) {

			Project projectR = new Project();
			projectR.setProjectId(proj.getProjectId());
			projectR.setProjectName(proj.getProjectName());
			projectR.setTower(proj.getTower());
			projectR.setProjectManagerId(employeeMaster);
			list.add(projectR);
		}
		projectRepository.saveAll(list);

		return "Project updated";

	}

	@Override
	public ResponseEntity<Object> importProjectDetails(MultipartFile file)
			throws ProjectException, MessagingException, EmployeeException, TowerException, IOException {
		logger.info("Imporing project Details from Project Service");
		if (file.isEmpty()) {
			logger.error(PLEASE_UPLOAD_THE_FILE);
			throw new FileNotFoundException(PLEASE_UPLOAD_THE_FILE);
		}

		Workbook workbook = getWorkBook(file);

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.iterator();
		rows.next();
		rows.next();

		while (rows.hasNext()) {
			Row result = rows.next();
			Cell projectName = result.getCell(1);
			Cell towerName = result.getCell(4);
			Cell manager = result.getCell(3);
			Integer managerId = (int) (manager.getNumericCellValue());
			Tower tower = towerRepository.findByTowerName(towerName.getStringCellValue());

			EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(managerId);

			if (employeeMaster == null) {
				logger.error(INVALID_EMPLOYEE_ID);
				throw new EmployeeException(INVALID_EMPLOYEE_ID);
			}

			if (!employeeMaster.getApplicationRoleMaster().getApplicationName().equalsIgnoreCase("Project Manager")) {
				logger.error(PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER);
				throw new EmployeeException("please check the employee id, not an project manager!");
			}

			if (tower == null) {
				logger.error(INVALID_TOWER_ID);
				throw new TowerException(INVALID_TOWER_ID);
			}

			Project projectNames = projectRepository.findByProjectManagerIdAndProjectNameAndTowerName(managerId,
					projectName.getStringCellValue(), towerName.getStringCellValue());
			if (projectNames != null) {
				logger.error(PROJECT_NAME_IS_ALREADY_EXIST);
				throw new ProjectException(PROJECT_NAME_IS_ALREADY_EXIST);
			}

			Project project = new Project();
			project.setProjectManagerId(employeeMaster);
			project.setTower(tower);
			project.setProjectManagerId(employeeMaster);
			projectRepository.save(project);

		}

		return new ResponseEntity<>("project Data uploaded ", HttpStatus.OK);

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

	public String importProject(MultipartFile file) throws ProjectException, TowerException, EmployeeException, IOException {

		logger.info("Importing project Details from project Service");
		if (file.isEmpty()) {
			logger.error(PLEASE_UPLOAD_THE_FILE);
			throw new ProjectException(PLEASE_UPLOAD_THE_FILE);
		}

		List<Project> list = new ArrayList<>();

		Workbook workbook = getWorkBook(file);

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.iterator();
		rows.next();

		while (rows.hasNext()) {
			Row result = rows.next();
			Cell c = result.getCell(1);

			if (c == null || c.getCellTypeEnum() == CellType.BLANK) {
				if (list.isEmpty()) {
					logger.error("project Details already existed");
					throw new ProjectException("project Details already existed");
				}
				return "project Data uploaded to database";
			}
			Tower tower = towerRepository.findByTowerName(result.getCell(4).getStringCellValue());
			EmployeeMaster employeeMaster = employeeRepository
					.findByEmployeeId((int) result.getCell(3).getNumericCellValue());

			if (employeeMaster == null) {
				logger.error(INVALID_EMPLOYEE_ID);
				throw new EmployeeException(INVALID_EMPLOYEE_ID);
			}

			if (tower == null) {
				logger.error(INVALID_TOWER_ID);
				throw new TowerException(INVALID_TOWER_ID);
			}

			Project projectName = projectRepository.findByProjectManagerIdAndProjectNameAndTowerTowerId(
					(int) result.getCell(3).getNumericCellValue(), result.getCell(1).getStringCellValue(),
					tower.getTowerId());
			if (projectName != null) {
				logger.error(PROJECT_NAME_IS_ALREADY_EXIST);
				throw new ProjectException(PROJECT_NAME_IS_ALREADY_EXIST);
			}
			Project project = new Project(0, result.getCell(1).getStringCellValue(), employeeMaster, tower);

			projectRepository.save(project);
			list.add(project);
		}

		return null;
	}

}
