package com.briup.environment.client;

import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.ConfigurationAWare;
import com.briup.environment.util.EmdcModule;
/*
 * Client接口是物联网数据中心项目网络模块客户端的规范
 * Client的作用就是与服务器进行通信，传递信息
 */
public interface Client extends EmdcModule,ConfigurationAWare{
	public void send(Collection<Environment> coll) throws Exception;
}
