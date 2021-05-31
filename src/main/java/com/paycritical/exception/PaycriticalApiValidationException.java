package com.paycritical.exception;

public class PaycriticalApiValidationException extends PaycriticalApiException {
	private static final long serialVersionUID = 9134097204146224111L;
	
	private String code;
	private String description;

	public PaycriticalApiValidationException(String code, 
			String description, 
			String message) {
		super(message);
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}