package com.devportal.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devportal.bean.URLMapping;
import com.devportal.constants.URLConstants;
import com.devportal.dao.URLMappingRepository;
import com.devportal.exceptions.DataNotFoundException;
import com.devportal.to.response.ShortenURLResponse;
import com.devportal.util.Util;

@Service
public class URLShortenerService {

	@Autowired
	private SessionFactory sessionFactroy;

	@Autowired
	private URLMappingRepository repository;

	@Autowired
	private Util util;

	private Random random = new Random();

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String shortenUrl(String originalUrl) {
		String shortCode = repository.findUrlValue(URLConstants.SHORT_CODE, URLConstants.ORIGINAL_URL, originalUrl,
				sessionFactroy);

		if (shortCode != null) {
			return shortCode;
		}

		return createAndStoreNewShortCode(originalUrl);
	}

	private String createAndStoreNewShortCode(String originalUrl) {
		String shortCode = generateUniqueShortCode();

		URLMapping mapping = new URLMapping();
		mapping.setOriginalUrl(originalUrl);
		mapping.setShortCode(shortCode);
		mapping.setCreatedAt(Util.getCurrentTimestamp());

		repository.save(mapping, sessionFactroy);
		util.saveDataInRedis(shortCode, originalUrl, 300);

		return shortCode;
	}

	private String generateUniqueShortCode() {
		String shortCode;

		do {
			shortCode = generateRandomShortCode();
		} while (repository.isShortCodeExist(shortCode, sessionFactroy));

		return shortCode;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String getOriginalUrl(String shortCode) {
		String originalUrl = fetchFromCacheOrDb(shortCode);

		if (originalUrl == null) {
			throw new DataNotFoundException(MessageFormat.format("URL not found for short code: {0}", shortCode));
		}

		Util.printLog(MessageFormat.format("Original URL: {0} for shortCode: {1}", originalUrl, shortCode));
		updateHitCount(shortCode);

		return originalUrl;
	}

	private String fetchFromCacheOrDb(String shortCode) {
		String url = util.getDataFromRedis(shortCode);

		if (null == url) {
			url = repository.findUrlValue(URLConstants.ORIGINAL_URL, URLConstants.SHORT_CODE, shortCode,
					sessionFactroy);
		}

		return url;
	}

	public void updateHitCount(String shortCode) {
		try {
			repository.updateHitCount(shortCode, sessionFactroy);
		} catch (Exception e) {
			Util.printError(MessageFormat.format("Failed to update hit count for short code: {0}", shortCode));
		}
	}

	private String generateRandomShortCode() {
		StringBuilder sb = new StringBuilder(6);
		for (int i = 0; i < 6; i++) {
			sb.append(URLConstants.ALLOWED_CHARS.charAt(random.nextInt(URLConstants.ALLOWED_CHARS_TOTAL_LENGTH)));
		}
		return sb.toString();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public void getAllURLMappings(ShortenURLResponse res, int offset, int limit) {
		List<URLMapping> mappingList = repository.getAllURLMappings(offset, limit, sessionFactroy);
		Long count = repository.getAllURLMappingsCount(sessionFactroy);

		if (null != mappingList && !mappingList.isEmpty()) {
			mappingList.forEach(entity -> Util.printLog(MessageFormat.format("Entity: {0}", entity)));
		}

		res.setUrlMappingList(mappingList);
		res.setCount(count);
	}
}
