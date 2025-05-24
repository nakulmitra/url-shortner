package com.devportal.constants;

public class URLConstants {

	private URLConstants() {

	}

	public static final String PREFIX = "http://devportal.com/";

	public static final String RETURN_SUCCESS_MSG = "Data returned successfuly";
	public static final String DATA_NOT_FOUND = "Data not found";
	public static final String CREATE_SUCCESS_MSG = "Data created successfuly";

	public static final String SHORT_CODE = "shortCode";
	public static final String ORIGINAL_URL = "originalUrl";
	public static final String CREATED_AT = "createdAt";

	public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final int ALLOWED_CHARS_TOTAL_LENGTH = ALLOWED_CHARS.length();

	public static final String ERROR_MSG = "Some error occur, please try again after some time";

}
