package com.devportal.to.response;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

public class ApiResponse {
	private HttpStatus status;
	private int statusCode;
	private String message;
	private boolean success;
	private Timestamp timestamp;

	public ApiResponse() {
		super();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
		this.statusCode = status.value();
	}

	public int getStatusCode() {
		return statusCode;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
