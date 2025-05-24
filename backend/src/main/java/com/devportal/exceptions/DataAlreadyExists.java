package com.devportal.exceptions;

public class DataAlreadyExists extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String data;

	public DataAlreadyExists(String data, String msg) {
		super(msg);
		this.data = data;
	}

	public String getData() {
		return data;
	}

}
