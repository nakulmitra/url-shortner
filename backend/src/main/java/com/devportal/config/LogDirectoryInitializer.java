package com.devportal.config;

import java.io.File;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.devportal.util.Util;

@Component
public class LogDirectoryInitializer {

	private static final String LOG_DIR_PATH = "logs";

	@PostConstruct
	public void createLogDirectory() {
		try {
			File logDir = new File(LOG_DIR_PATH);
			if (!logDir.exists()) {
				boolean created = logDir.mkdirs();
				if (created) {
					Util.printLog(MessageFormat.format("Log directory created at: {0}", logDir.getAbsolutePath()));
				} else {
					Util.printError("Failed to create log directory. It may already exist or permission is denied.");
				}
			} else {
				Util.printLog(MessageFormat.format("Log directory already exists at: {0}", logDir.getAbsolutePath()));
			}
		} catch (Exception e) {
			Util.printError(MessageFormat.format("Error while creating log directory: {0}", e.getMessage()));
		}
	}
}