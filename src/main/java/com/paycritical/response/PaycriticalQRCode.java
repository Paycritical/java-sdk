package com.paycritical.response;

// Wrapper class to read the QRcode response using jackson
public class PaycriticalQRCode {
	private String qrCodeId;

	public String getQrCodeId() {
		return qrCodeId;
	}

	public void setQrCodeId(String qrCodeId) {
		this.qrCodeId = qrCodeId;
	}
	
}
