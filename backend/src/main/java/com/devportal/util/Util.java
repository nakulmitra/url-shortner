package com.devportal.util;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.devportal.to.response.ApiResponse;

@SuppressWarnings({"deprecation"})
@Component
public class Util {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public static <T extends ApiResponse> T createFailureResp(Class<T> respClass, String msg) {
		T response = null;
		try {
			response = respClass.newInstance();
			response.setMessage(msg);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setSuccess(true);
			response.setTimestamp(getCurrentTimestamp());
		} catch (InstantiationException | IllegalAccessException e) {
			printError("Inside catch block for method createSuccessResp under Util class");
		}

		return response;
	}

	public static <T extends ApiResponse> T createSuccessResp(Class<T> respClass, String msg, HttpStatus respCode) {
		T response = null;
		try {
			response = respClass.newInstance();
			response.setMessage(msg);
			response.setStatus(respCode);
			response.setSuccess(true);
			response.setTimestamp(getCurrentTimestamp());
		} catch (InstantiationException | IllegalAccessException e) {
			printError("Inside catch block for method createSuccessResp under Util class");
		}

		return response;
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static void printLog(String msg) {
		LOGGER.debug(msg);
	}
	
	public static void printError(String msg) {
		LOGGER.error(msg);
	}
	
	public void saveDataInRedis(String key, String value, long timeout) {
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.opsForValue().getAndExpire(key, timeout, TimeUnit.SECONDS);
	}
	
	public Object getDataFromRedis(String key) {
		return redisTemplate.opsForValue().get(key);
	}

}
