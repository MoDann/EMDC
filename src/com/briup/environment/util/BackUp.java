package com.briup.environment.util;

public interface BackUp extends EmdcModule,ConfigurationAWare{
	//在保存的同时覆盖原来的数据
	boolean STORE_OVERRIDE=false;
	//追加
	boolean STORE_APPEND=true;
	//删除备份文件
	boolean LOAD_REMOVE=true;
	//在读取的同时，保存备份文件
	boolean LOAD_UNREMOVE=false;
	
	public void store(String filePath, Object obj,boolean append) throws Exception;
	public Object load(String filePath, boolean del) throws Exception;

}
