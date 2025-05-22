package com.devportal.exceptions;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String msg) {
		super(msg);
	}

}
