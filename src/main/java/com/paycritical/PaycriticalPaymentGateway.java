package com.paycritical;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paycritical.exception.*;
import com.paycritical.response.PaycriticalAuthorization;
import com.paycritical.response.ValidationSummary;
import com.paycritical.response.PaycriticalPayment;
import com.paycritical.response.PaycriticalPaymentStatus;
import com.paycritical.response.PaycriticalQRCode;
import com.paycritical.response.PaycriticalQRCodeStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.HashMap;

/**
 * Ticket HTTP Request class. Implements all the API methods specified in:
 * https://tr05sbx.paycritical.com/swagger/ui/index
 * 
 * @version 1.0
 */
public class PaycriticalPaymentGateway {
	private String apiKey;
	private String baseUrl;

	/**
	 * <p>
	 * Initialize HTTP Request Client
	 * </p>
	 * 
	 * @param APIKey  API Authentication key. According to the documentation this
	 *                has the format of: Basic <string>
	 * @param BaseUrl The base URL of the API. According to the documentation, the
	 *                base URL for the sandbox POS API is
	 *                "https://tr05sbx.paycritical.com"
	 */
	public PaycriticalPaymentGateway(String APIKey, String BaseUrl) {
		this.apiKey = APIKey;
		this.baseUrl = BaseUrl;
	}

	/**
	 * <p>
	 * Get Payment Status
	 * </p>
	 * 
	 * Makes a GET Request to the API. This GET request asks for the payment status
	 * using the url "{BaseUrl}/api/payment/{paymentId}".
	 * 
	 * @param paymentId The payment ID to get the status from.
	 * @return A PaycriticalPaymentStatus object.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public PaycriticalPaymentStatus getPaymentStatus(String paymentId) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// HTTPClient to make the GET request
		HttpClient client = HttpClient.newHttpClient();
		// Build the GET request with the request builder
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/payment/" + paymentId))
				.setHeader("Content-Type", "application/json") // Request content type
				.setHeader("Accept", "application/json") // The accepted MIME type
				.setHeader("Authorization", apiKey) // Authorization API key
				.GET() // Make GET request
				.build(); // Build the request

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);

		return new ObjectMapper().readValue(response.body(), PaycriticalPaymentStatus.class);

	}

	/**
	 * <p>
	 * Get Authorization Details
	 * </p>
	 * 
	 * Makes a GET Request to the API. This GET request asks for the details of an Authorization
	 * using the url "{BaseUrl}/api/payment/{paymentId}/authorization".
	 * 
	 * @param paymentId The payment ID to get the status from.
	 * @return A PaycriticalAuthorization object.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public PaycriticalAuthorization getAuthorizationDetails(String paymentId) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// HTTPClient to make the GET request
		HttpClient client = HttpClient.newHttpClient();
		// Build the GET request with the request builder
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/api/payment/" + paymentId + "/authorization"))
				.setHeader("Content-Type", "application/json") // Request content type
				.setHeader("Accept", "application/json") // The accepted MIME type
				.setHeader("Authorization", apiKey) // Authorization API key
				.GET() // Make GET request
				.build(); // Build the request

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);

		return new ObjectMapper().readValue(response.body(), PaycriticalAuthorization.class);
	}

	/**
	 * <p>
	 * Request Authorization
	 * </p>
	 * 
	 * Makes a POST Request to the API. This POST sends a payment request with type
	 * "Authorization" using the url "{BaseUrl}/api/payment".
	 * 
	 * @param amount          The payment amount to be requested.
	 * @param orderRef        The internal order reference.
	 * @param phoneNumber     The phone number to which the request is sent
	 * @return A PaycriticalPayment object.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public PaycriticalPayment requestAuthorization(double amount, String phoneNumber, String orderRef)
			throws IOException, InterruptedException, PaycriticalApiInternalServerErrorException,
			PaycriticalApiValidationException, PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException,
			PaycriticalApiException {
		// Key - Value pairs in HashMap to be used with the jackson JSON parser
		@SuppressWarnings("serial")
		var values = new HashMap<String, String>() {
			{
				put("amount", Double.toString(amount));
				put("orderRef", orderRef);
				put("phoneNumber", phoneNumber);
				put("transactionType", "Authorization");
			}
		};

		// initialize an object mapper for the jackson JSON parser
		var objectMapper = new ObjectMapper();
		// Use the jackson JSON parser to get the JSON string corresponding to the
		// values HashMap
		String requestBody = objectMapper.writeValueAsString(values);

		// HTTPClient to make the POST request
		HttpClient client = HttpClient.newHttpClient();
		// Build the POST request with the request builder
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/payment")) 
				.setHeader("Content-Type", "application/json") // Request content type
				.setHeader("Accept", "application/json") // The accepted MIME type
				.setHeader("Authorization", apiKey) // Authorization API key
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Make POST request
				.build(); // Build the request

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);

		return new ObjectMapper().readValue(response.body(), PaycriticalPayment.class);
	}

	/**
	 * <p>
	 * Request Payment
	 * </p>
	 * 
	 * Makes a POST Request to the API. This POST sends a payment request with type
	 * "Capture" using the url "{BaseUrl}/api/payment".
	 * 
	 * @param amount          The payment amount to be requested.
	 * @param orderRef        The internal order reference.
	 * @param phoneNumber     The phone number to which the request is sent
	 * @return A PaycriticalPayment object.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public PaycriticalPayment requestPayment(double amount, String phoneNumber, String orderRef) throws IOException,
			InterruptedException, PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// Key - Value pairs in HashMap to be used with the jackson JSON parser
		@SuppressWarnings("serial")
		var values = new HashMap<String, String>() {
			{
				put("amount", Double.toString(amount));
				put("orderRef", orderRef);
				put("phoneNumber", phoneNumber);
				put("transactionType", "Capture");
			}
		};

		// initialize an object mapper for the jackson JSON parser
		var objectMapper = new ObjectMapper();
		// Use the jackson JSON parser to get the JSON string corresponding to the
		// values HashMap
		String requestBody = objectMapper.writeValueAsString(values);

		// HTTPClient to make the POST request
		HttpClient client = HttpClient.newHttpClient();
		// Build the POST request with the request builder
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/payment")) 
				.setHeader("Content-Type", "application/json") // Request content type
				.setHeader("Accept", "application/json") // The accepted MIME type
				.setHeader("Authorization", apiKey) // Authorization API key
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Make POST request
				.build(); // Build the request

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);

		return new ObjectMapper().readValue(response.body(), PaycriticalPayment.class);
	}

	/**
	 * <p>
	 * Resend Payment Notification
	 * </p>
	 * 
	 * Makes a POST Request to the API. This POST resends a payment request using
	 * the url "{BaseUrl}/api/payment/resend".
	 * 
	 * @param paymentId The payment ID of the payment to be resent.
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public void resendPayment(String paymentId) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// Key - Value pairs in HashMap to be used with the jackson JSON parser
		@SuppressWarnings("serial")
		var values = new HashMap<String, String>() {
			{
				put("paymentId", paymentId);
			}
		};

		// initialize an object mapper for the jackson JSON parser
		var objectMapper = new ObjectMapper();
		// Use the jackson JSON parser to get the JSON string corresponding to the
		// values HashMap
		String requestBody = objectMapper.writeValueAsString(values);

		// HTTPClient to make the POST request
		HttpClient client = HttpClient.newHttpClient();
		// Build the POST request with the request builder
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/payment/resend"))
				.setHeader("Content-Type", "application/json").setHeader("Accept", "application/json")
				.setHeader("Authorization", apiKey).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);
	}

	/**
	 * <p>
	 * Request Qr Code
	 * </p>
	 * 
	 * Makes a POST Request to the API. This POST requests a QRCode using the url
	 * "{BaseUrl}/api/qrcode".
	 * 
	 * @param amount   The amount to be associated with the generated QR Code.
	 * @param orderRef The internal order reference associated with the generated QR
	 *                 Code.
	 * @return A PaycriticalQRCode object.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public PaycriticalQRCode requestQRCode(double amount, String orderRef) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// Key - Value pairs in HashMap to be used with the jackson JSON parser
		@SuppressWarnings("serial")
		var values = new HashMap<String, String>() {
			{
				put("amount", Double.toString(amount));
				put("orderRef", orderRef);
			}
		};

		// initialize an object mapper for the jackson JSON parser
		var objectMapper = new ObjectMapper();
		// Use the jackson JSON parser to get the JSON string corresponding to the
		// values HashMap
		String requestBody = objectMapper.writeValueAsString(values);

		// HTTPClient to make the POST request
		HttpClient client = HttpClient.newHttpClient();
		// Build the POST request with the request builder
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/api/qrcode"))
				.setHeader("Content-Type", "application/json")
				.setHeader("Accept", "application/json")
				.setHeader("Authorization", apiKey)
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);

		return new ObjectMapper().readValue(response.body(), PaycriticalQRCode.class);
	}

	/**
	 * <p>
	 * Get Qr Code Status
	 * </p>
	 * 
	 * Makes a GET Request to the API. This GET requests a QRCode using the url
	 * "{BaseUrl}/api/qrCode/{qrCodeId}".
	 * 
	 * @param amount   The amount to be associated with the generated QR Code.
	 * @param qrCodeId The Id of the generated QR
	 *                 Code.
	 * @return A PaycriticalQRCodeStatus object.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public PaycriticalQRCodeStatus getQRCodeStatus(String qrCodeId) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// HTTPClient to make the GET request
		HttpClient client = HttpClient.newHttpClient();
		// Build the GET request with the request builder
		HttpRequest request = HttpRequest.newBuilder().version(Version.HTTP_1_1)
				.uri(URI.create(baseUrl + "/api/qrCode/" + qrCodeId))
				.setHeader("Content-Type", "application/json")
				.setHeader("Accept", "application/json")
				.setHeader("Authorization", apiKey)
				.GET()
				.build();

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);
		
		if (response.statusCode() == 204)
        {
            return new PaycriticalQRCodeStatus();
        }

		return new ObjectMapper().readValue(response.body(), PaycriticalQRCodeStatus.class);
	}

	/**
	 * <p>
	 * Cancel Payment
	 * </p>
	 * 
	 * Makes a PUT Request to the API. This PUT cancels a payment using the url
	 * "{BaseUrl}/api/payment/cancel".
	 * 
	 * @param paymentId The payment ID of the payment to be canceled.
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public void cancelPaymentRequest(String paymentId) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// Key - Value pairs in HashMap to be used with the jackson JSON parser
		@SuppressWarnings("serial")
		var values = new HashMap<String, String>() {
			{
				put("paymentId", paymentId);
			}
		};

		// initialize an object mapper for the jackson JSON parser
		var objectMapper = new ObjectMapper();
		// Use the jackson JSON parser to get the JSON string corresponding to the
		// values HashMap
		String requestBody = objectMapper.writeValueAsString(values);

		// HTTPClient to make the PUT request
		HttpClient client = HttpClient.newHttpClient();
		// Build the PUT request with the request builder
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/payment/cancel"))
				.setHeader("Content-Type", "application/json").setHeader("Accept", "application/json")
				.setHeader("Authorization", apiKey).PUT(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);
	}

	/**
	 * <p>
	 * Refund Payment
	 * </p>
	 * 
	 * Makes a PUT Request to the API. This PUT refunds a payment using the url
	 * "{BaseUrl}/api/payment/refund".
	 * 
	 * @param paymentId The payment ID of the payment to be refunded.
	 * @param amount    The amount to be refunded.
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public void refundPaymentRequest(String paymentId, double amount) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// Key - Value pairs in HashMap to be used with the jackson JSON parser
		@SuppressWarnings("serial")
		var values = new HashMap<String, String>() {
			{
				put("paymentId", paymentId);
				put("amount", Double.toString(amount));
			}
		};

		// initialize an object mapper for the jackson JSON parser
		var objectMapper = new ObjectMapper();
		// Use the jackson JSON parser to get the JSON string corresponding to the
		// values HashMap
		String requestBody = objectMapper.writeValueAsString(values);

		// HTTPClient to make the PUT request
		HttpClient client = HttpClient.newHttpClient();
		// Build the PUT request with the request builder
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/payment/refund"))
				.setHeader("Content-Type", "application/json").setHeader("Accept", "application/json")
				.setHeader("Authorization", apiKey).PUT(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);
	}

	/**
	 * <p>
	 * Capture Payment
	 * </p>
	 * 
	 * Makes a PUT Request to the API. This PUT captures a payment using the url
	 * "{BaseUrl}/api/payment/capture".
	 * 
	 * @param paymentId The payment ID of the payment to be captured.
	 * @param amount    The amount to be captured.
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PaycriticalApiForbiddenException The provided authentication is incorrect.
	 * @throws PaycriticalApiUnauthorizedException Basic Authentication not set.
	 * @throws PaycriticalApiValidationException Code and Description of what went wrong is provided at the exception body.
	 * @throws PaycriticalApiInternalServerErrorException An unexpected error occurred. A eventId is returned.
	 */
	public void capturePaymentRequest(String paymentId, double amount) throws IOException, InterruptedException,
			PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		// Key - Value pairs in HashMap to be used with the jackson JSON parser
		@SuppressWarnings("serial")
		var values = new HashMap<String, String>() {
			{
				put("paymentId", paymentId);
				put("amount", Double.toString(amount));
			}
		};

		// initialize an object mapper for the jackson JSON parser
		var objectMapper = new ObjectMapper();
		// Use the jackson JSON parser to get the JSON string corresponding to the
		// values HashMap
		String requestBody = objectMapper.writeValueAsString(values);

		// HTTPClient to make the PUT request
		HttpClient client = HttpClient.newHttpClient();
		// Build the PUT request with the request builder
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/api/payment/capture"))
				.setHeader("Content-Type", "application/json").setHeader("Accept", "application/json")
				.setHeader("Authorization", apiKey).PUT(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		// Capture the response resulting from the request
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ValidateResponse(response);
	}

	private void ValidateResponse(HttpResponse<String> response)
			throws PaycriticalApiInternalServerErrorException, PaycriticalApiValidationException,
			PaycriticalApiUnauthorizedException, PaycriticalApiForbiddenException, PaycriticalApiException {
		var statusCode = response.statusCode();

		if (statusCode >= 200 && statusCode <= 299) {
			return;
		}

		switch (statusCode) {
		case 400:
			var objectMapper = new ObjectMapper();
			ValidationSummary error;
			try {
				error = objectMapper.readValue(response.body(), ValidationSummary.class);
			} catch (Exception e) {
				error = new ValidationSummary();
				error.setCode("400");
				error.setDescription("The request is invalid.");
			}
			throw new PaycriticalApiValidationException(error.getCode(), error.getDescription(), "");
		case 401:
			throw new PaycriticalApiUnauthorizedException(response.body());
		case 403:
			throw new PaycriticalApiForbiddenException(response.body());
		case 500:
			throw new PaycriticalApiInternalServerErrorException(response.body());
		default:
			throw new PaycriticalApiException(response.body());
		}
	}

}
