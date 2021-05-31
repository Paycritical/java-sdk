package com.paycritical.response;

public class PaycriticalCapture {
	private String paymentId;
	private String paymentHumanId;
	private Double amount;

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
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
