package com.briup.environment.util;

/*
 * 该接口提供了日志模块的规范，日志模块将日志信息划分为5个级别
 */
public interface Logger1 extends EmdcModule {
	//记录debug级别日志
	public void debug(String msg);
	//记录info级别日志
	public void info(String msg);
	//记录warn级别日志
	public void warn(String msg);
	//记录error级别日志
	public void error(String msg);
	//记录fatal级别日志（崩溃）
	public void fatal(String msg);


}
