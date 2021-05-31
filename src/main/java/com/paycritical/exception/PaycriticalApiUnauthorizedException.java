package com.paycritical.exception;

public class PaycriticalApiUnauthorizedException extends PaycriticalApiException {
	private static final long serialVersionUID = 4577168961809363499L;

	public PaycriticalApiUnauthorizedException(String message) {
		super(message);
	}
}