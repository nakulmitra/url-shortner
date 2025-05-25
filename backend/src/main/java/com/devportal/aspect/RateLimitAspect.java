package com.devportal.aspect;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.devportal.annotation.RateLimit;
import com.devportal.exceptions.RateLimitException;
import com.devportal.util.Util;

@Aspect
@Component
public class RateLimitAspect {

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Before("@annotation(rateLimit)")
	public void validateRateLimit(RateLimit rateLimit) {
		String key = rateLimit.key() + "-" + req.getRemoteAddr();

		boolean allowed = isAllowed(key, rateLimit.maxLimit(), rateLimit.windowInSeconds());
		if (!allowed) {
			throw new RateLimitException(MessageFormat.format("Rate limit exceeded for {0}", rateLimit.key()));
		}
	}

	private boolean isAllowed(String redisKey, int maxLimit, int windowSize) {
		boolean requestAllowed = true;

		long now = System.currentTimeMillis();
		redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, now - (windowSize * 1000L));

		String key = String.valueOf(now) + "-" + UUID.randomUUID();
		Util.printLog(MessageFormat.format("Redis Key: {0} \nKey: {1}", redisKey, key));

		redisTemplate.opsForZSet().add(redisKey, key, now);

		Long count = redisTemplate.opsForZSet().zCard(redisKey);
		redisTemplate.expire(redisKey, Duration.ofSeconds(windowSize));

		if (count != null && count > maxLimit) {
			requestAllowed = false;
		}

		return requestAllowed;
	}
}
