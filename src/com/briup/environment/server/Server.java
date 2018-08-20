package com.briup.environment.server;


/*
 * Server用于启动服务器，开始接收客户端发送的信息并且调用DBStore将接收的数据持久化
 */
public interface Server {
	//public Collection<Environment> revicer() throws Exception;
	public void revicer() throws Exception;
	//该方法使用Server安全的停止运行
	public void shutdown() throws Exception;

}
