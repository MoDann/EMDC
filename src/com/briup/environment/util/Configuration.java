package com.briup.environment.util;

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.gui.Login;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server1;

/*
 * Configuration接口提供了配置模块的规范，配置模块通过Gather，Client，Server，DBStore等模块的实现进行实例传递，
 * 通过配置模块可以获取各个模块的实例
 */
public interface Configuration {

	//获取日志模块的实例
	public Logger1 getLogger()throws Exception;
	//获取备份模块的实例
	public BackUp getBackup()throws Exception;
	//获取采集模块的实例
	public Gather getGather() throws Exception;
	//获取客户端模块的实例
	public Client getClient() throws Exception;
	//获取服务器模块的实例
	public Server1 getServer() throws Exception;
	//获取入库模块的实例
	public DBStore getDBStore() throws Exception;
	//获取登录模块的实例
	public Login getLogin() throws Exception;
	
}
