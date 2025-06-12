package com.devportal.async;

import java.text.MessageFormat;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devportal.dao.URLMappingRepository;
import com.devportal.util.Util;

@Service
public class HitCountUpdater {

	@Autowired
	private SessionFactory sessionFactroy;

	@Autowired
	private URLMappingRepository repository;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Async
	@Transactional
	public void updateHitCountAsync(String shortCode) {
		try {
			repository.updateHitCount(shortCode, sessionFactroy);
		} catch (Exception ex) {
			Util.printError(MessageFormat.format("Failed to update hit count for short code: {0}", shortCode));
		}
	}

	public void recordHitCount(String shortCode) {
		redisTemplate.opsForValue().increment("hitCount:" + shortCode);
	}

}
