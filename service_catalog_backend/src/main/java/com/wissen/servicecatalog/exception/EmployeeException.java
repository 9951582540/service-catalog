package com.wissen.servicecatalog.exception;

import java.util.List;

public class EmployeeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EmployeeException(String message) {
		super(message);
	}
	public EmployeeException(List<String> message) {
		
	}
	public EmployeeException(String message, Throwable t) {
		super(message, t);
	}
}
