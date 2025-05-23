package com.devportal.util;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.devportal.constants.URLConstants;
import com.devportal.to.response.ApiResponse;

@SuppressWarnings({ "deprecation" })
@Component
public class Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public static <T extends ApiResponse> T createFailureResp(Class<T> respClass, String msg, HttpStatus status) {
		T response = null;
		try {
			response = respClass.newInstance();
			response.setMessage(msg);
			response.setStatus(status);
			response.setSuccess(false);
			response.setTimestamp(getCurrentTimestamp());
		} catch (InstantiationException | IllegalAccessException e) {
			printError("Inside catch block for method createSuccessResp under Util class");
		}

		return response;
	}

	public static <T extends ApiResponse> T createSuccessResp(Class<T> respClass, String msg, HttpStatus status) {
		T response = null;
		try {
			response = respClass.newInstance();
			response.setMessage(msg);
			response.setStatus(status);
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
		try {
			redisTemplate.opsForValue().set(key, value);
			setExpiryInRedis(key, timeout);
			printLog(MessageFormat.format("Saved key {0} with timeout {1}s to Redis", key, timeout));
		} catch (DataAccessException e) {
			printError(MessageFormat.format("Failed to save data in Redis for key: {0} \ndue to error: {1}", key,
					e.getMessage()));
		} catch (Exception e) {
			printError(MessageFormat.format("Unexpected failure while saving to Redis for key: {0} \ndue to error: {1}",
					key, e.getMessage()));
		}
	}

	public String getDataFromRedis(String key) {
		try {
			String value = redisTemplate.opsForValue().get(key);
			printLog(MessageFormat.format("Retrieved key {0} from Redis with value: {1}", key, value));
			return value;
		} catch (DataAccessException e) {
			printError(MessageFormat.format("Failed to get data from Redis for key: {0} \ndue to error: {1}", key,
					e.getMessage()));
		} catch (Exception e) {
			printError(MessageFormat.format(
					"Unexpected failure while retrieving from Redis for key: {0} \ndue to error: {1}", key,
					e.getMessage()));
		}
		return null;
	}

	public void setExpiryInRedis(String key, long timeout) {
		try {
			redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
			printLog(MessageFormat.format("Set expiry {0}s for key {1} in Redis", timeout, key));
		} catch (DataAccessException e) {
			printError(MessageFormat.format("Failed to set expiry in Redis for key: {0} \ndue to error: {1}", key,
					e.getMessage()));
		} catch (Exception e) {
			printError(MessageFormat.format(
					"Unexpected failure while setting expiry in Redis for key: {0} \ndue to error: {1}", key,
					e.getMessage()));
		}
	}

	public static String extractShortCode(String shortUrl) {
		return shortUrl.substring(URLConstants.PREFIX.length());
	}

}
