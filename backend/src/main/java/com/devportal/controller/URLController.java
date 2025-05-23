package com.devportal.controller;

import java.text.MessageFormat;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devportal.annotation.RateLimit;
import com.devportal.constants.URLConstants;
import com.devportal.service.URLShortenerService;
import com.devportal.to.request.ShortenURLRequest;
import com.devportal.to.response.ShortenURLResponse;
import com.devportal.util.Util;
import com.devportal.validation.URLValidation;

@RestController
@RequestMapping("/api")
public class URLController {

	@Autowired
	private URLShortenerService service;

	@RateLimit(key = "shorten-url", maxLimit = 2, windowInSeconds = 60)
	@PostMapping(value = "/shorten")
	public ShortenURLResponse shortenUrl(@RequestBody @Valid ShortenURLRequest request) {
		ShortenURLResponse res = Util.createSuccessResp(ShortenURLResponse.class, URLConstants.CREATE_SUCCESS_MSG,
				HttpStatus.CREATED);

		String shortCode = service.shortenUrl(request.getUrl());
		Util.printLog(MessageFormat.format("Short Code: {0}", shortCode));
		res.setShortURL(shortCode);

		return res;
	}

	@PostMapping(value = "/expand")
	public ShortenURLResponse expandUrl(@RequestBody @Valid ShortenURLRequest request) {
		String shortURL = request.getUrl();
		if (!URLValidation.hasValidPrefix(shortURL)) {
			Util.printError(MessageFormat.format("Invalid short URL prefix for URL: {0}", shortURL));
			return buildErrorResponse("Invalid short URL prefix", HttpStatus.BAD_REQUEST);
		}

		String shortCode = Util.extractShortCode(shortURL);
		if (!URLValidation.isValidShortCode(shortCode)) {
			Util.printError(MessageFormat.format("Invalid short URL format for URL: {0}", shortURL));
			return buildErrorResponse("Invalid short URL format", HttpStatus.BAD_REQUEST);
		}

		return getOriginalUrl(shortCode);
	}

	private ShortenURLResponse buildErrorResponse(String message, HttpStatus status) {
		return Util.createFailureResp(ShortenURLResponse.class, message, status);
	}

	private ShortenURLResponse getOriginalUrl(String shortCode) {
		String originalUrl = service.getOriginalUrl(shortCode);

		ShortenURLResponse response = Util.createSuccessResp(ShortenURLResponse.class, URLConstants.RETURN_SUCCESS_MSG,
				HttpStatus.OK);
		response.setOriginalURL(originalUrl);

		return response;
	}

	@GetMapping(value = "/getAllURLMappings")
	public ShortenURLResponse getAllURLMappings(@RequestParam(name = "offset", defaultValue = "0") int offset,
			@RequestParam(name = "limit", defaultValue = "10") int limit) {
		ShortenURLResponse res = Util.createSuccessResp(ShortenURLResponse.class, URLConstants.RETURN_SUCCESS_MSG,
				HttpStatus.OK);

		service.getAllURLMappings(res, offset, limit);
		if (res.getCount() == 0) {
			res.setMessage(URLConstants.DATA_NOT_FOUND);
		}

		return res;
	}

}
