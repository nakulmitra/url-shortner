package com.devportal.controller;

import java.text.MessageFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devportal.RateLimit;
import com.devportal.service.UrlShortenerService;
import com.devportal.to.response.ShortenUrlResponse;

@Controller
@RequestMapping("/api")
public class UrlController {

	@Autowired
	private UrlShortenerService service;

	private Logger LOGGER = LoggerFactory.getLogger(UrlController.class);

	@RateLimit(key = "shorten")
	@RequestMapping(value = "/shorten", method = RequestMethod.POST)
	@ResponseBody
	public ShortenUrlResponse shortenUrl(@RequestBody Map<String, String> request) {
		String shortCode = service.shortenUrl(request.get("longUrl"));
		LOGGER.info(MessageFormat.format("Short Code: {0}", shortCode));

		ShortenUrlResponse res = new ShortenUrlResponse(200, "Data Returned Successfuly", true);
		res.setShortCode(shortCode);

		return res;
	}

	@RequestMapping(value = "/expand/{shortURL}", method = RequestMethod.GET)
	@ResponseBody
	public ShortenUrlResponse expandUrl(@PathVariable String shortURL) {
		ShortenUrlResponse res = null;
		try {
			String originalUrl = service.getOriginalUrl(shortURL);
			res = new ShortenUrlResponse(200, "Data Returned Successfuly", true);
			res.setOriginalURL(originalUrl);
		} catch (RuntimeException e) {
			res = new ShortenUrlResponse(500, e.getMessage(), false);
		}

		return res;
	}

	@RequestMapping(value = "/getAllMappings", method = RequestMethod.GET)
	@ResponseBody
	public ShortenUrlResponse getAllMappings() {
		ShortenUrlResponse res = new ShortenUrlResponse(200, "Data Returned Successfuly", true);
		service.getAllMappings(res);
		if (res.getCount() == 0) {
			res.setMessage("Data Not Found");
		}

		return res;
	}

}
