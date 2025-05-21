package com.devportal.aspect;

import java.time.Duration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.devportal.RateLimit;

@Aspect
@Component
public class RateLimitAspect {

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	private Logger LOGGER = LoggerFactory.getLogger(RateLimitAspect.class);

	@Before("@annotation(rateLimit)")
	public void validateRateLimit(RateLimit rateLimit) throws Exception {
		String key = rateLimit.key() + "-" + req.getRemoteAddr();

		boolean allowed = isAllowed(key, rateLimit.maxLimit(), rateLimit.windowInSeconds());
		if (!allowed) {
			throw new Exception("Rate limit exceeded for " + rateLimit.key());
		}
	}

	private boolean isAllowed(String redisKey, int maxLimit, int windowSize) {
		long now = System.currentTimeMillis();

		redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, now - (windowSize * 1000L));
		
		String key = String.valueOf(now) + "-" + UUID.randomUUID();
		LOGGER.debug("Key: " + key);
		redisTemplate.opsForZSet().add(redisKey, key, now);

		Long count = redisTemplate.opsForZSet().zCard(redisKey);
		redisTemplate.expire(redisKey, Duration.ofSeconds(60));

		if (count != null && count > maxLimit) {
			return false;
		}

		return true;
	}
}
