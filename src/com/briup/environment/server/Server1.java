package com.briup.environment.server;

import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.ConfigurationAWare;
import com.briup.environment.util.EmdcModule;

public interface Server1 extends EmdcModule,ConfigurationAWare{

	public void revicer() throws Exception;
	public void shutdown() throws Exception;
}
