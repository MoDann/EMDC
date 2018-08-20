package com.briup.environment.util;

/**
 * 当某模块实现了该接口，那么配置模块在初始化该
 * 模块的同时会将自身的引用传递给该M模块。
 * 
 * @author wang
 * @version new {@link Date}
 */
public interface ConfigurationAWare{
	
	/**
	 * 该方法用于传递配置模块
	 * 
	 * @param configuration
	 *            传递的配置模块
	 */
	
	void setConfiguration(Configuration configuration);
}