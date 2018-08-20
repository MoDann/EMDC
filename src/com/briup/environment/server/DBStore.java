package com.briup.environment.server;

import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.ConfigurationAWare;
import com.briup.environment.util.EmdcModule;

public interface DBStore extends EmdcModule,ConfigurationAWare{

	public void saveDB(Collection<Environment > c) throws Exception;
}
