package com.devportal.scheduler;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.devportal.dao.URLMappingRepository;

@Component
@EnableScheduling
public class HitCountAggregator {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private URLMappingRepository repository;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Scheduled(fixedRate = 30000)
	@Transactional
	public void aggregateHitCount() {
		Set<String> keys = redisTemplate.keys("hitCount:*");
		if (null == keys) {
			return;
		}

		for (String key : keys) {
			String shortCode = key.split(":")[1];
			String hitCountStr = redisTemplate.opsForValue().get(key);
			Long count = hitCountStr != null ? Long.valueOf(hitCountStr) : 0;

			if (count > 0) {
				repository.updateHitCount(shortCode, count, sessionFactory);
				redisTemplate.delete(key);
			}
		}
	}

}
