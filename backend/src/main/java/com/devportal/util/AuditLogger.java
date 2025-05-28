package com.devportal.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

	private static Logger AUDIT_LOGGER = LoggerFactory.getLogger("AUDIT_LOGGER");

	public void log(String action, HttpServletRequest request) {
		String ip = getClientIp(request);
		AUDIT_LOGGER.trace("AUDIT | IP: {} | Action: {}", ip, action);
	}

	private String getClientIp(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		if (forwarded != null && !forwarded.isEmpty()) {
			return forwarded.split(",")[0];
		}
		return request.getRemoteAddr();
	}

}
