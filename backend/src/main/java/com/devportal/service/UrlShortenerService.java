package com.devportal.service;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devportal.bean.UrlMapping;
import com.devportal.dao.UrlMappingRepository;
import com.devportal.to.response.ShortenUrlResponse;

@Service
public class UrlShortenerService {

	@Autowired
	private SessionFactory sessionFactroy;

	@Autowired
	private UrlMappingRepository repository;
	
	private Random random = new Random();
	
	private String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private Logger LOGGER = LoggerFactory.getLogger(UrlShortenerService.class);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String shortenUrl(String originalUrl) {
		UrlMapping mapping = repository.findByOriginalUrl(originalUrl, sessionFactroy);
		if (null != mapping) {
			return mapping.getShortCode();
		}

		return saveUrlMapping(originalUrl);
	}

	private String saveUrlMapping(String originalUrl) {
		String shortCode = generateShortCode();
		while (repository.isShortCodeExist(shortCode, sessionFactroy)) {
			shortCode = generateShortCode();
		}
		
		UrlMapping mapping = new UrlMapping();
		mapping.setOriginalUrl(originalUrl);
		mapping.setShortCode(shortCode);
		mapping.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		repository.save(mapping, sessionFactroy);

		return shortCode;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String getOriginalUrl(String shortCode) {
		UrlMapping mapping = repository.findByShortCode(shortCode, sessionFactroy);
		if (null == mapping) {
			throw new RuntimeException("URL not found");
		}

		LOGGER.info(MessageFormat.format("Original URL: {0}", mapping.getOriginalUrl()));
		repository.updateHitCount(shortCode, mapping.getHitCount() + 1, sessionFactroy);
		return mapping.getOriginalUrl();
	}

	private String generateShortCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public void getAllMappings(ShortenUrlResponse res) {
		List<UrlMapping> mappingList = repository.getAllMappings(0, 10, sessionFactroy);
		Long count = repository.getAllMappingsCount(sessionFactroy);

		if (null != mappingList && !mappingList.isEmpty()) {
			mappingList.forEach(entity -> {
				LOGGER.info(MessageFormat.format("Entity: {0}\n", entity));
			});
		}
		
		res.setUrlMappingList(mappingList);
		res.setCount(count);
	}
}
