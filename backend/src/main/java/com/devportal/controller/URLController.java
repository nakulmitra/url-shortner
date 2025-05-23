package com.devportal.controller;

import java.text.MessageFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devportal.annotation.RateLimit;
import com.devportal.constants.UrlConstants;
import com.devportal.service.UrlShortenerService;
import com.devportal.to.response.ShortenUrlResponse;
import com.devportal.util.Util;

@RestController
@RequestMapping("/api")
public class UrlController {

	@Autowired
	private UrlShortenerService service;

	@RateLimit(key = "shorten-url", maxLimit = 2, windowInSeconds = 60)
	@PostMapping(value = "/shorten")
	public ShortenUrlResponse shortenUrl(@RequestBody Map<String, String> request) {
		ShortenUrlResponse res = Util.createSuccessResp(ShortenUrlResponse.class, UrlConstants.CREATE_SUCCESS_MSG,
				HttpStatus.CREATED);

		String shortCode = service.shortenUrl(request.get("longUrl"));
		Util.printLog(MessageFormat.format("Short Code: {0}", shortCode));
		res.setShortCode(shortCode);

		return res;
	}

	@GetMapping(value = "/expand/{shortURL}")
	public ShortenUrlResponse expandUrl(@PathVariable String shortURL) {
		ShortenUrlResponse res = Util.createSuccessResp(ShortenUrlResponse.class, UrlConstants.RETURN_SUCCESS_MSG,
				HttpStatus.OK);

		String originalUrl = service.getOriginalUrl(shortURL);
		res.setOriginalURL(originalUrl);

		return res;
	}

	@GetMapping(value = "/getAllMappings")
	public ShortenUrlResponse getAllMappings() {
		ShortenUrlResponse res = Util.createSuccessResp(ShortenUrlResponse.class, UrlConstants.RETURN_SUCCESS_MSG,
				HttpStatus.OK);

		service.getAllMappings(res);
		if (res.getCount() == 0) {
			res.setMessage(UrlConstants.DATA_NOT_FOUND);
		}

		return res;
	}

}
