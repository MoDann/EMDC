package com.briup.environment.util;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/*
 * 该接口是除配置模块外的所有模块的父接口
 * 用于模块接收初始化配置信息和注入配置模块
 */
public interface EmdcModule extends ConfigurationAWare {

	/*
	 * 将所需要的配置信息传递进该类，该类得到配置信息后进行初始化，
	 * 建议在执行该类其他方法前先执行这个方法
	 */
	
	public void init(Properties properties) throws Exception;
	
}
