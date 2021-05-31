package com.paycritical.response;

public class PaycriticalQRCodeStatus {
	private String paymentId;
	private String paymentHumanId;
	private String status;
	
	public PaycriticalQRCodeStatus() {
		this.status = "Requested";
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentHumanId() {
		return paymentHumanId;
	}

	public void setPaymentHumanId(String paymentHumanId) {
		this.paymentHumanId = paymentHumanId;
	}
}
