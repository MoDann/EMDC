package com.briup.environment.util;

public interface Log extends EmdcModule{
	public void debug(String msg);
	public void info(String msg);
	public void warn(String msg);
	public void error(String msg);
	public void fatal(String msg);

}
