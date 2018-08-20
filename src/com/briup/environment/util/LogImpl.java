package com.briup.environment.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogImpl implements Log {
	private String path="src/log4j.properties";
	private Logger defaultLogger=Logger.getLogger(path);
	//private Logger defaultLogger=Logger.getRootLogger();

	@Override
	public void debug(String msg) {
		defaultLogger.debug(msg);
	}

	@Override
	public void info(String msg) {
		defaultLogger.info(msg);
	}

	@Override
	public void warn(String msg) {
		defaultLogger.warn(msg);
	}

	@Override
	public void error(String msg) {
		defaultLogger.error(msg);
	}

	@Override
	public void fatal(String msg) {
		defaultLogger.fatal(msg);
		
	}
//	public static void main(String[] args) {
//		LogImpl log = new LogImpl();
//		log.info("this is info");
//		log.debug("this is debug");
//		log.warn("this is warn");
//		log.error("this is error");
//		log.fatal("this is fatal");
//	}

	@Override
	public void init(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		
	}

}
