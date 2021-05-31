package com.paycritical.response;

import java.util.Date;
import java.util.List;

public class PaycriticalAuthorization {
	private String status;
	private String expiration;
	private Double remainingAmount;
	private List<PaycriticalCapture> captures;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public Double getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(Double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public List<PaycriticalCapture> getCaptures() {
		return captures;
	}
	public void setCaptures(List<PaycriticalCapture> captures) {
		this.captures = captures;
	}
}
