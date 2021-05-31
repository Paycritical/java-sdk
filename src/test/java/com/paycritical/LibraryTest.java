package com.paycritical;

import org.junit.FixMethodOrder;
import org.junit.Test;

import com.paycritical.response.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class LibraryTest {
	static String URL = "http://localhost:59605";
	static String TOKEN = "Basic b09Ca2VldXA6b09Ca2VldXB4S204UVBETWhlWWkwTTVoeXpMcTVCa1A=";
	
	static String qrCodeId;
	static String requestedPaymentId;
	
    @Test
    public void t01requestQRCode() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
			PaycriticalQRCode res = classUnderTest.requestQRCode(0.1, "AAAA");
			assertNotNull(res);
			qrCodeId = res.getQrCodeId();
		} catch (Exception e) {
			assertNull(e);
		}
    	
    	assertNotNull(qrCodeId);
		System.out.println(qrCodeId);
    }
    

    @Test
    public void t02getQRCodeStatus() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalQRCodeStatus res = classUnderTest.getQRCodeStatus(qrCodeId);
    		assertNotNull(res);
    		assertEquals("Requested", res.getStatus());
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t03getCompletedQRCodeStatus() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalQRCodeStatus res = classUnderTest.getQRCodeStatus("f6530724-b143-4df6-8a10-b346b435e7b0");
    		assertNotNull(res);
    		assertEquals("Completed", res.getStatus());
    		assertEquals("LQ50EWPO", res.getPaymentHumanId());
    		assertEquals("6d419e63-6c0d-4331-bbe6-e80586d6299c", res.getPaymentId());

    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t04getPaymentStatus() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalPaymentStatus res = classUnderTest.getPaymentStatus("6d419e63-6c0d-4331-bbe6-e80586d6299c");
    		assertNotNull(res);
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t05requestAuthorization() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalPayment res = classUnderTest.requestAuthorization(1.23, "+351911111111", java.util.UUID.randomUUID().toString());
    		assertNotNull(res);
    		assertEquals("Requested", res.getStatus());
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    @Test
    public void t06GetAuthorizationDetails() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalAuthorization res = classUnderTest.getAuthorizationDetails("c14af8ec-d2f4-4d9d-a351-2ce119cb9bb9");
    		assertNotNull(res);
    		assertEquals("RejectedByUser", res.getStatus());
    		assertEquals("2021-09-25T14:10:43.017", res.getExpiration());
    		assertEquals("1.23", res.getRemainingAmount().toString());


    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t07requestPayment() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalPayment res = classUnderTest.requestPayment(1.25, "+351911111111", java.util.UUID.randomUUID().toString());
    		assertNotNull(res);
    		assertEquals("Requested", res.getStatus());
    		requestedPaymentId = res.getPaymentId();
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t08resendPayment() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		classUnderTest.resendPayment(requestedPaymentId);
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t10cancelPaymentRequest() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalPayment paymentRequest = classUnderTest.requestPayment(1.25, "+351911111111", java.util.UUID.randomUUID().toString());
    		classUnderTest.cancelPaymentRequest(paymentRequest.getPaymentId());
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t11refundPaymentRequest() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalPayment paymentRequest = classUnderTest.requestPayment(1.25, "+351911111111", java.util.UUID.randomUUID().toString());
    		
    		//add breakpoint here and accept payment manually from simulator
//    		classUnderTest.refundPaymentRequest(paymentRequest.getPaymentId(), 1.25);
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
    @Test
    public void t12capturePaymentRequest() {
        PaycriticalPaymentGateway classUnderTest = new PaycriticalPaymentGateway(TOKEN, URL);
        
    	try {
    		PaycriticalPayment authorization = classUnderTest.requestAuthorization(1.23, "+351911111111", java.util.UUID.randomUUID().toString());

    		//add breakpoint here and accept payment manually from simulator
//    		classUnderTest.capturePaymentRequest(authorization.getPaymentId(), 1.23);
//    		
//    		PaycriticalPaymentStatus paymentSatus = classUnderTest.getPaymentStatus(authorization.getPaymentId());
//    		
//    		assertEquals("Completed", paymentSatus.getStatus());
//    		assertEquals("Authorization", paymentSatus.getTransactionType());
//    		
//    		var authorizationDetails = classUnderTest.getAuthorizationDetails(authorization.getPaymentId());
//    		
//    		assertEquals(1, authorizationDetails.getCaptures().size());
    		
    	} catch (Exception e) {
			assertNull(e);
		}
    }
    
}
