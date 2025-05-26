package com.devportal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devportal.constants.URLConstants;
import com.devportal.to.response.ApiResponse;
import com.devportal.to.response.ShortenURLResponse;
import com.devportal.util.Util;

@RestControllerAdvice
public class GlobalExceptionHandling {

	@ExceptionHandler(DataAlreadyExists.class)
	public ShortenURLResponse handleDataAlreadyExists(DataAlreadyExists ex) {
		ShortenURLResponse response = Util.createSuccessResp(ShortenURLResponse.class, ex.getMessage(), HttpStatus.OK);
		response.setShortURL(ex.getData());

		return response;
	}

	@ExceptionHandler(RateLimitException.class)
	public ApiResponse handleRateLimitException(RateLimitException ex) {
		return Util.createFailureResp(ApiResponse.class, ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ApiResponse handleDataNotFoundException(DataNotFoundException ex) {
		return Util.createFailureResp(ApiResponse.class, ex.getMessage(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse handleException(Exception ex) {
		return Util.createFailureResp(ApiResponse.class, URLConstants.ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
