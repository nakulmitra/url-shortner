package com.devportal.controller;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
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
import com.devportal.util.AuditLogger;
import com.devportal.util.Util;
import com.devportal.validation.URLValidation;

@RestController
@RequestMapping("/api")
public class URLController {

	@Autowired
	private URLShortenerService service;

	@Autowired
	private AuditLogger auditLogger;

	@Autowired
	private HttpServletRequest httpRequest;

	@RateLimit(key = "shorten-url", maxLimit = 2, windowInSeconds = 60)
	@PostMapping(value = "/shorten")
	public ShortenURLResponse shortenUrl(@RequestBody @Valid ShortenURLRequest request) {
		String shortCode = service.shortenUrl(request.getUrl());
		Util.printLog(MessageFormat.format("Short Code: {0}", shortCode));

		ShortenURLResponse res = Util.createSuccessResp(ShortenURLResponse.class, URLConstants.CREATE_SUCCESS_MSG,
				HttpStatus.CREATED);
		res.setShortURL(shortCode);

		auditLogger.log(MessageFormat.format("Shorten URL for Original URL: {0}", request.getUrl()), httpRequest);
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

		auditLogger.log(MessageFormat.format("Expanded URL for shortcode: {0}", shortCode), httpRequest);
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

		auditLogger.log("Get all URLs", httpRequest);
		return res;
	}

}
