package com.briup.environment.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerTest {
	public static void main(String[] args) {
		Logger logger = Logger.getRootLogger();
		PropertyConfigurator.configure("src/log4j1.properties");
		logger.debug("this is debug");
		logger.info("this is info");
		logger.warn("this is warn");
	}

}
