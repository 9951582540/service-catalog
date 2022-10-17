package com.wissen.servicecatalog.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.entity.TowerScoreSkillDetails;
import com.wissen.servicecatalog.exception.ActivityException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ActivityGlobalChanges;
import com.wissen.servicecatalog.pojo.ActivityRequest;
import com.wissen.servicecatalog.pojo.ActivityResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.repository.TowerSkillScoreRepository;
import com.wissen.servicecatalog.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {
	private static final String INVALID_TOWER = "Invalid Tower";

	private static final String CATEGORY_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK = "Category Cell should not be null/Cell type should be string/blank";
	private static final String SERVICE_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK = "service Cell should not be null/cell type should be string/blank";
	private static final String NO_COLUMN_CALLED_CATEGORY = "No column called category";

	private static final String PLEASE_UPLOAD_THE_FILE = "Please upload the file";

	private static final String INVALID_ACTIVITY_ID = "Invalid Activity ID";

	private static final String INVALID_TOWER_ID = "Invalid Tower id";

	Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TowerRepository towerRepository;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	TowerSkillScoreRepository towerScoreRepository;

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private List<Activity> listActivity;

	public ActivityServiceImpl(List<Activity> listActivity) {
		this.listActivity = listActivity;
		workbook = new XSSFWorkbook();
	}

	@Override
	public List<ActivityResponse> addActivity(List<ActivityRequest> activityRequest) throws TowerException {
		logger.info("Adding the activity from Activity Service");
		List<Activity> save = new LinkedList<>();
		List<ActivityResponse> activityResponses = new LinkedList<>();

		if (activityRequest.isEmpty()) {
			logger.error("Please enter the activities");
			throw new TowerException("Please enter the activities");
		}
		for (ActivityRequest activitys : activityRequest) {

			Skill skill = skillRepository.findBySkillLevel(activitys.getSkillLevel());

			Tower tower = towerRepository.findByTowerId(activitys.getTowerId());
			if (tower == null) {
				logger.error(INVALID_TOWER_ID);
				throw new TowerException(INVALID_TOWER_ID);
			}
			Activity duplicates = activityRepository.findByDuplicates(activitys.getCategory(), activitys.getService(),
					activitys.getTechnologies(), activitys.getActivityName(), activitys.getSkillLevel(),
					activitys.getFacilitator(), tower.getTowerName());
			if (duplicates != null)
				continue;

			if (skill == null) {
				ActivityResponse response = new ActivityResponse();
				Activity activity = new Activity();
				activity.setActivityName(activitys.getActivityName());
				activity.setCategory(activitys.getCategory());
				activity.setFacilitator(activitys.getFacilitator());
				activity.setService(activitys.getService());
				activity.setTechnologies(activitys.getTechnologies());
				Skill newLevelSkill = new Skill();
				newLevelSkill.setSkillLevel(activitys.getSkillLevel());
				skillRepository.save(newLevelSkill);
				activity.setSkill(newLevelSkill);
				activity.setTower(tower);
				save.add(activity);
				Activity result = activityRepository.save(activity);

				response.setSkill(activitys.getSkillLevel());
				response.setActivityId(result.getActivityId());
				response.setActivityName(activity.getActivityName());
				response.setCategory(activity.getCategory());
				response.setFacilitator(activity.getFacilitator());
				response.setService(activity.getService());
				response.setTechnologies(activity.getTechnologies());
				activityResponses.add(response);

				response.setSkill(activitys.getSkillLevel());

			} else {
				ActivityResponse response = new ActivityResponse();
				Activity activity = new Activity();
				activity.setActivityName(activitys.getActivityName());
				activity.setCategory(activitys.getCategory());
				activity.setFacilitator(activitys.getFacilitator());
				activity.setService(activitys.getService());
				activity.setTechnologies(activitys.getTechnologies());
				activity.setSkill(skill);
				activity.setTower(tower);
				Activity result = activityRepository.save(activity);
				response.setSkill(activitys.getSkillLevel());
				response.setActivityId(result.getActivityId());
				response.setActivityName(activity.getActivityName());
				response.setCategory(activity.getCategory());
				response.setFacilitator(activity.getFacilitator());
				response.setService(activity.getService());
				response.setTechnologies(activity.getTechnologies());
				activityResponses.add(response);
			}

		}
		if (activityResponses.isEmpty() && !activityRequest.isEmpty()) {
			logger.error("Cant add duplicate activity's to the respective tower");
			throw new TowerException("Cant add duplicate activity's to the respective tower");
		}
		return activityResponses;
	}

	@Override

	public List<ActivityResponse> getTowerId(Integer towerId) throws TowerException {
		logger.info("Getting Tower Id from Activity Service");
		ModelMapper modelMapper = new ModelMapper();
		if (towerId == null) {
			logger.error("Please enter the tower id");
			throw new TowerException("Please enter the tower id");

		}
		Tower tower = towerRepository.findByTowerId(towerId);
		if (tower == null) {
			logger.error(INVALID_TOWER_ID);
			throw new TowerException(INVALID_TOWER_ID);

		}
		List<Activity> activityList = activityRepository.getActivity(tower.getTowerId());
		if (activityList.isEmpty()) {
			logger.error("no activites in this tower");
			throw new TowerException("no activites in this tower");
		}
		List<ActivityResponse> activityResponseList = new LinkedList<>();
		for (Activity activity : activityList) {
			ActivityResponse activityResponse = modelMapper.map(activity, ActivityResponse.class);
			activityResponse.setSkill(activity.getSkill().getSkillLevel());
			activityResponseList.add(activityResponse);
		}
		return activityResponseList;

	}

	@Override
	public List<ActivityResponse> updateActivity(List<ActivityRequest> activityRequest)
			throws ActivityException, TowerException {
		logger.info("Updating Activity from Activity Service");

		List<ActivityResponse> activityResponse = new LinkedList<>();

		for (ActivityRequest list : activityRequest) {

			Activity activity = activityRepository.findByActivityId(list.getActivityId());

			if (activity == null) {
				logger.error(INVALID_ACTIVITY_ID);
				throw new ActivityException(INVALID_ACTIVITY_ID);
			}
			Skill skill = skillRepository.findBySkillLevel(list.getSkillLevel());

			Tower tower = towerRepository.findByTowerId(list.getTowerId());
			if (tower == null) {
				logger.error(INVALID_TOWER_ID);
				throw new TowerException(INVALID_TOWER_ID);
			}
			if (skill == null) {
				ActivityResponse response = new ActivityResponse();

				Skill newLevelSkill = new Skill();
				newLevelSkill.setSkillLevel(list.getSkillLevel());
				skillRepository.save(newLevelSkill);
				activity.setSkill(newLevelSkill);
				activity.setTower(tower);
				activity.setActivityName(list.getActivityName());
				activity.setActivityId(list.getActivityId());
				activity.setCategory(list.getCategory());
				activity.setFacilitator(list.getFacilitator());
				activity.setService(list.getService());
				activity.setTechnologies(list.getTechnologies());
				activity.setTower(tower);
				activityRepository.save(activity);

				response.setActivityId(list.getActivityId());
				response.setActivityName(list.getActivityName());
				response.setCategory(list.getCategory());
				response.setFacilitator(list.getFacilitator());
				response.setService(list.getService());
				response.setSkill(newLevelSkill.getSkillLevel());
				response.setTechnologies(list.getTechnologies());

				activityResponse.add(response);
			}
			ActivityResponse response = new ActivityResponse();
			Activity activityR = new Activity();
			activityR.setSkill(skill);
			activityR.setTower(tower);
			activityR.setActivityName(list.getActivityName());
			activityR.setActivityId(list.getActivityId());
			activityR.setCategory(list.getCategory());
			activityR.setFacilitator(list.getFacilitator());
			activityR.setService(list.getService());
			activityR.setTechnologies(list.getTechnologies());
			activityR.setTower(tower);
			activityRepository.save(activityR);

			response.setActivityId(list.getActivityId());
			response.setActivityName(list.getActivityName());
			response.setCategory(list.getCategory());
			response.setFacilitator(list.getFacilitator());
			response.setService(list.getService());
			response.setSkill(list.getSkillLevel());
			response.setTechnologies(list.getTechnologies());

			activityResponse.add(response);

		}

		return activityResponse;
	}

	public Workbook getWorkBook(MultipartFile file) throws TowerException, IOException {
		logger.info("Getting Workbook from Activity Service");

		if (file.isEmpty()) {
			logger.error(PLEASE_UPLOAD_THE_FILE);
			throw new TowerException(PLEASE_UPLOAD_THE_FILE);
		}

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

	public String importTowerActivity(MultipartFile file, String towerName, Integer sheetNumber)
			throws TowerException, ActivityException, IOException {
		logger.info("Importing Tower Activity from Activity Service");
		if (file.isEmpty()) {
			logger.error(PLEASE_UPLOAD_THE_FILE);
			throw new FileNotFoundException(PLEASE_UPLOAD_THE_FILE);
		}
		Workbook workbookFile = getWorkBook(file);
		Sheet sheetFile = null;
		try {
			sheetFile = workbookFile.getSheetAt(sheetNumber);
		} catch (IllegalArgumentException e) {
			logger.error("Please enter the correct sheet number!");
			throw new TowerException("Please enter the correct sheet number!");
		}
		Iterator<Row> rows = sheetFile.iterator();
		rows.next();
		boolean category = false;
		while (rows.hasNext()) {
			Row result = rows.next();
			Cell c = result.getCell(1);

			if (c == null) {
				logger.error(NO_COLUMN_CALLED_CATEGORY);
				throw new TowerException(NO_COLUMN_CALLED_CATEGORY);
			}

			if (result.getCell(1).getCellTypeEnum() == CellType.STRING
					&& (result.getCell(1).getStringCellValue().equals("Category")
							|| result.getCell(1).getStringCellValue().equals("Category "))) {

				category = checkCategory(rows, towerName);
				break;
			}
		}
		if (category) {
			return "Activity Data's Uploaded";
		} else {
			logger.error(NO_COLUMN_CALLED_CATEGORY);
			throw new TowerException(NO_COLUMN_CALLED_CATEGORY);
		}
	}

	public boolean checkCategory(Iterator<Row> rows, String towerName) throws TowerException, ActivityException {

		List<Activity> save = new LinkedList<>();
		logger.info("Checking Category from Activity Service");
		while (rows.hasNext()) {
			Row result = rows.next();
			Cell cell = result.getCell(1);
			if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
				break;
			}
			if (cell == null || cell.getCellTypeEnum() != CellType.STRING || cell.getCellTypeEnum() == CellType.BLANK) {
				logger.error(CATEGORY_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK);
				throw new ActivityException(CATEGORY_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK);
			}
			cell = result.getCell(2);

			if (cell == null || cell.getCellTypeEnum() != CellType.STRING || cell.getCellTypeEnum() == CellType.BLANK) {
				logger.error(SERVICE_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK);
				throw new ActivityException(SERVICE_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK);

			}

			if (result.getCell(8).getStringCellValue().equalsIgnoreCase(towerName)
					|| result.getCell(7).getStringCellValue().equalsIgnoreCase(towerName)) {

				Skill skill = skillRepository.findBySkillLevel(result.getCell(5).getStringCellValue());

				Tower tower = towerRepository.findByTowerName(towerName);

				if (tower == null) {
					logger.error(INVALID_TOWER);
					throw new TowerException(INVALID_TOWER);
				}

				Activity duplicates = activityRepository.findByDuplicates(result.getCell(1).getStringCellValue(),
						result.getCell(2).getStringCellValue(), result.getCell(3).getStringCellValue(),
						result.getCell(4).getStringCellValue(), result.getCell(5).getStringCellValue(),
						result.getCell(6).getStringCellValue(), towerName);
				if (duplicates != null)
					continue;
				if (skill == null) {
					Activity activity = new Activity();
					Skill newLevelSkill = new Skill();
					newLevelSkill.setSkillLevel(result.getCell(5).getStringCellValue());
					skillRepository.save(newLevelSkill);
					activity.setSkill(newLevelSkill);
					activity.setTower(tower);
					activity.setCategory(result.getCell(1).getStringCellValue());
					activity.setTechnologies(result.getCell(3).getStringCellValue());
					activity.setService(result.getCell(2).getStringCellValue());
					activity.setActivityName(result.getCell(4).getStringCellValue());
					activity.setFacilitator(result.getCell(6).getStringCellValue());

					save.add(activity);
				} else {
					Activity activity = new Activity();
					activity.setSkill(skill);
					activity.setTower(tower);
					activity.setCategory(result.getCell(1).getStringCellValue());
					activity.setTechnologies(result.getCell(3).getStringCellValue());
					activity.setService(result.getCell(2).getStringCellValue());
					activity.setActivityName(result.getCell(4).getStringCellValue());
					activity.setFacilitator(result.getCell(6).getStringCellValue());
					save.add(activity);
				}
			} else {
				throw new TowerException(INVALID_TOWER);
			}

		}
		Set<Activity> set = new HashSet<>(save);
		List<Activity> list = activityRepository.saveAll(set);
		if (list.isEmpty()) {
			logger.error("Activity Data's are already saved");
			throw new TowerException("Activity Data's are already saved");
		}
		return true;
	}

	public String deleteActivity(Integer activityId) throws ActivityException {
		logger.info("Deleting Activity from Activity service");
		if (activityId == null) {
			logger.error("Please enter the activity ID");
			throw new ActivityException("Please enter the activity ID");
		}
		Activity activity = activityRepository.findByActivityId(activityId);
		if (activity == null) {
			logger.error(INVALID_ACTIVITY_ID);
			throw new ActivityException(INVALID_ACTIVITY_ID);
		}
		activityRepository.delete(activity);

		return "Activity Deleted";
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		logger.info("Creating cell from Activity service");
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
		logger.info("Writing header line from Activity service");

		sheet = workbook.createSheet("Activity_details");

		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(10);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		createCell(row, 0, "Tower Activity Information", style);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		font.setFontHeightInPoints((short) (7));

		row = sheet.createRow(1);
		font.setBold(true);
		font.setFontHeight(12);
		style.setFont(font);
		createCell(row, 0, "Sl/no", style);
		createCell(row, 1, "Category", style);
		createCell(row, 2, "Service", style);
		createCell(row, 3, "Technologies", style);
		createCell(row, 4, "Activity name", style);
		createCell(row, 5, "Skill", style);
		createCell(row, 6, "Facilitator", style);
		createCell(row, 7, "Tower", style);
	}

	private void writeDataLines() throws ActivityException {
		logger.info("Writing data lines from activity service");
		int rowCount = 2;
		if (listActivity.isEmpty()) {
			logger.error("No activity data found");
			throw new ActivityException("No activity data found");
		}
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		int serialNo = 0;
		for (Activity stu : listActivity) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, ++serialNo, style);
			createCell(row, columnCount++, stu.getCategory(), style);
			createCell(row, columnCount++, stu.getService(), style);
			createCell(row, columnCount++, stu.getTechnologies(), style);
			createCell(row, columnCount++, stu.getActivityName(), style);
			createCell(row, columnCount++, stu.getSkill().getSkillLevel(), style);
			createCell(row, columnCount++, stu.getFacilitator(), style);
			createCell(row, columnCount, stu.getTower().getTowerName(), style);
		}
	}

	public void export(HttpServletResponse response) throws IOException, ActivityException {
		logger.info("Exporting from Activity service");
		writeHeaderLine();
		writeDataLines();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	@Override
	public ActivityGlobalChanges updateGlobalChanges(ActivityGlobalChanges activityGlobalChanges)
			throws TowerException {
		for (Integer id : activityGlobalChanges.getTowerId()) {

			Tower tower = towerRepository.findByTowerId(id);
			if (tower == null) {
				logger.error(INVALID_TOWER_ID);
				throw new TowerException(INVALID_TOWER_ID);
			}

			Activity duplicates = activityRepository.findByDuplicates(activityGlobalChanges.getCategory(),
					activityGlobalChanges.getService(), activityGlobalChanges.getTechnologies(),
					activityGlobalChanges.getActivityName(), activityGlobalChanges.getSkillLevel(),
					activityGlobalChanges.getFacilitator(), tower.getTowerName());

			if (duplicates != null)
				continue;

			Skill skill = skillRepository.findBySkillLevel(activityGlobalChanges.getSkillLevel());

			if (skill == null) {

				Skill newSkill = new Skill();
				newSkill.setSkillLevel(activityGlobalChanges.getSkillLevel());
				skillRepository.save(newSkill);

				Activity activity = new Activity();
				activity.setActivityName(activityGlobalChanges.getActivityName());
				activity.setCategory(activityGlobalChanges.getCategory());
				activity.setFacilitator(activityGlobalChanges.getFacilitator());
				activity.setService(activityGlobalChanges.getService());
				activity.setSkill(newSkill);
				activity.setTechnologies(activityGlobalChanges.getTechnologies());
				activity.setTower(tower);
				activityRepository.save(activity);
				TowerScoreSkillDetails towerScoreSkillDetail = new TowerScoreSkillDetails();

				towerScoreSkillDetail.setMaximumScore(activityGlobalChanges.getMaximumScore());
				towerScoreSkillDetail.setMinimumScore(activityGlobalChanges.getMinimumScore());
				towerScoreSkillDetail.setNextLevelMin(activityGlobalChanges.getNextLevelMin());
				towerScoreSkillDetail.setSkill(newSkill);
				towerScoreSkillDetail.setTower(tower);
				towerScoreRepository.save(towerScoreSkillDetail);

			} else {

				Activity activity = new Activity();
				activity.setActivityName(activityGlobalChanges.getActivityName());
				activity.setCategory(activityGlobalChanges.getCategory());
				activity.setFacilitator(activityGlobalChanges.getFacilitator());
				activity.setService(activityGlobalChanges.getService());
				activity.setSkill(skill);
				activity.setTechnologies(activityGlobalChanges.getTechnologies());
				activity.setTower(tower);
				activityRepository.save(activity);
				TowerScoreSkillDetails findByTowerSkill = towerScoreRepository.findByTowerSkill(id,
						activityGlobalChanges.getSkillLevel());
				if (findByTowerSkill == null) {

					TowerScoreSkillDetails towerScoreSkillDetail = new TowerScoreSkillDetails();
					towerScoreSkillDetail.setMaximumScore(activityGlobalChanges.getMaximumScore());
					towerScoreSkillDetail.setMinimumScore(activityGlobalChanges.getMinimumScore());
					towerScoreSkillDetail.setNextLevelMin(activityGlobalChanges.getNextLevelMin());
					towerScoreSkillDetail.setSkill(skill);
					towerScoreSkillDetail.setTower(tower);
					towerScoreRepository.save(towerScoreSkillDetail);
				} else {
					TowerScoreSkillDetails towerScoreSkillDetail = new TowerScoreSkillDetails();
					towerScoreSkillDetail.setTowerSkillDetailsId(findByTowerSkill.getTowerSkillDetailsId());
					towerScoreSkillDetail.setMaximumScore(activityGlobalChanges.getMaximumScore());
					towerScoreSkillDetail.setMinimumScore(activityGlobalChanges.getMinimumScore());
					towerScoreSkillDetail.setNextLevelMin(activityGlobalChanges.getNextLevelMin());
					towerScoreSkillDetail.setSkill(skill);
					towerScoreSkillDetail.setTower(tower);
					towerScoreRepository.save(towerScoreSkillDetail);
				}

			}

		}

		return activityGlobalChanges;

	}
}
