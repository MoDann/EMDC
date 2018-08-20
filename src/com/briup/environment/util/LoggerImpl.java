package com.briup.environment.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerImpl implements Logger1{
	
	private String filePath;
	private static Logger logger;
	static{
		logger=Logger.getRootLogger();
		//PropertyConfigurator.configure("src/log4j1.properties");
//		 PropertyConfigurator.configure(filePath);
	}

	@Override
	public void init(Properties properties) throws Exception {
		filePath=properties.getProperty("log-file");
        PropertyConfigurator.configure(filePath);
	}
	
	@Override
	public void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
	}

	@Override
	public void error(String msg) {
		logger.error(msg);
	}

	@Override
	public void fatal(String msg) {
		logger.fatal(msg);
	}
	public static void main(String[] args) throws Exception {
		ConfigurationImpl conf=new ConfigurationImpl();
		LoggerImpl logger = (LoggerImpl) conf.getLogger();
		logger.debug("this is debug");
		logger.info("this is info");
		logger.warn("this is warn");
		logger.error("this is error");
		logger.fatal("this is fatal");
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		
	}

}
