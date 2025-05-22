package com.devportal.exceptions;

public class RateLimitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RateLimitException(String msg) {
		super(msg);
	}
}
