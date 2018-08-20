package com.briup.environment.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

/**
 * 备份模块
 * @author ASUS
 *
 */
public class BackUpImpl implements BackUp {
	
	//private String directoryname="back_tmp";
	//private static Logger1 logger = new LoggerImpl();

	private String directoryname;
	private static LoggerImpl logger;
	private Configuration configuration;
	
	@Override
	public void init(Properties properties) throws Exception {
		 System.out.println(directoryname);
	     directoryname=properties.getProperty("backup-file");
	}
	
	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration=configuration;
		try {
			logger=(LoggerImpl) configuration.getLogger();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public void store(String filePath, Object obj, boolean append) throws Exception {
		File file = new File(directoryname);
		if (!file.exists()) {
			file.mkdir();
		}
		File file2 = new File(file,filePath);
		logger.info("读取文件："+file2.getName());
		// 如果append为true的话，则将字节写到文件末尾
		FileOutputStream fos = new FileOutputStream(file2,append);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		fos.close();
	}

	@Override
	public Object load(String filePath, boolean del) throws Exception{
		File file = new File(directoryname);
		if (!file.exists()) {
			return null;
		}
		File file2 = new File(file,filePath);
		if (!file2.exists()) {
			return null;
		}
		FileInputStream fis = new FileInputStream(file2);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object data = ois.readObject();
		ois.close();
		fis.close();
		if (del) {
			file2.delete();
		}
		return data;
	}
	
//	@SuppressWarnings("unused")
//	public static void main(String[] args) throws Exception {
//		ConfigurationImpl conf=new ConfigurationImpl();
//		BackUpImpl back = (BackUpImpl) conf.getBackup();
//		back.store("a.txt", "success", BackUp.STORE_OVERRIDE);
//		back.store("b.txt", "hello", BackUp.STORE_APPEND);
//		Object object = back.load("a.txt", BackUp.LOAD_UNREMOVE);
//		Object object2 = back.load("b.txt", BackUp.LOAD_UNREMOVE);
//		System.out.println(object2.toString());
//	}

}
