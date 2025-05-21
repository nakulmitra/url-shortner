package com.devportal.to.response;

import java.time.LocalDateTime;

public class ApiResponse {
	private int statusCode;
	private String message;
	private boolean success;
	private LocalDateTime timestamp;

	public ApiResponse(int statusCode, String message, boolean success) {
		this.statusCode = statusCode;
		this.message = message;
		this.success = success;
		this.timestamp = LocalDateTime.now();
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
