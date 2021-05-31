package com.paycritical.exception;

public class PaycriticalApiInternalServerErrorException extends PaycriticalApiException {
	private static final long serialVersionUID = -6698998165033095119L;

	public PaycriticalApiInternalServerErrorException(String message) {
		super(message);
	}
}