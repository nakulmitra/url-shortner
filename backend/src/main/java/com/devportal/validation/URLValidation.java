package com.devportal.validation;

import com.devportal.constants.URLConstants;

public class URLValidation {
	
	private URLValidation() {

	}

	public static boolean hasValidPrefix(String shortUrl) {
		return shortUrl != null && shortUrl.startsWith(URLConstants.PREFIX);
	}

	public static boolean isValidShortCode(String shortCode) {
		return shortCode != null && shortCode.matches("^[a-zA-Z0-9]{6}$");
	}

}
