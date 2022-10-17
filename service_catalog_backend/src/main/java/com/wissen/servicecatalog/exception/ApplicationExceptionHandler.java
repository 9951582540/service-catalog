package com.wissen.servicecatalog.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	private static final String MESSAGE = "message";
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		Map<String, String> errorMap = new HashMap<>();
		List<FieldError> list = exception.getBindingResult().getFieldErrors();
		for (FieldError errors : list)
			errorMap.put(MESSAGE, errors.getDefaultMessage());
		return errorMap;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(TowerException.class)
	public Map<String, String> handleTowerException(TowerException exception) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(MESSAGE, exception.getMessage());

		return errorMap;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(FeedbackException.class)
	public Map<String, String> handleFeedbackException(FeedbackException exception) {
		Map<String, String> errorMap = new HashMap<>();

		errorMap.put(MESSAGE, exception.getMessage());
		return errorMap;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ActivityException.class)
	public Map<String, String> handleActivityException(ActivityException exception) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(MESSAGE, exception.getMessage());
		return errorMap;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(EmployeeException.class)
	public Map<String, String> handleEmployeeException(EmployeeException exception) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(MESSAGE, exception.getMessage());

		return errorMap;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ProjectException.class)
	public Map<String, String> handleProjectException(ProjectException exception) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(MESSAGE, exception.getMessage());
		return errorMap;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ScoreException.class)
	public Map<String, String> handleScorceException(ScoreException exception) {

		Map<String, String> errorMap = new HashMap<>();

		errorMap.put(MESSAGE, exception.getMessage());

		return errorMap;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(StatusException.class)
	public Map<String, String> handleStatusException(StatusException exception) {

		Map<String, String> map = new HashMap<>();
		map.put(MESSAGE, exception.getMessage());
		return map;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(MessagingException.class)
	public Map<String, String> handleMessageException(MessagingException exception) {
		Map<String, String> map = new HashMap<>();
		map.put(MESSAGE, exception.getMessage());
		return map;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SkillException.class)
	public Map<String, String> handleSkillException(SkillException exception) {

		Map<String, String> map = new HashMap<>();
		map.put(MESSAGE, exception.getMessage());
		return map;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SettingException.class)
	public Map<String, String> handleSettingException(SettingException exception) {

		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(MESSAGE, exception.getMessage());

		return errorMap;
	}
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(MultipartException.class)
	public Map<String,String> handleMultipartException(MultipartException exception){
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(MESSAGE, exception.getMessage());

		return errorMap;
	}
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ExpiredJwtException.class)
	public Map<String, String> handleExpiredJwtException(ExpiredJwtException exception) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put(MESSAGE, exception.getMessage());

		return errorMap;
	}
}
