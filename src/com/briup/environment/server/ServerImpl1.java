package com.briup.environment.server;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackUp;
import com.briup.environment.util.BackUpImpl;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationImpl;
import com.briup.environment.util.Logger1;
import com.briup.environment.util.LoggerImpl;

public class ServerImpl1 implements Server1{

	//private int port=9999;
	private ServerSocket ss;
	private Socket socket;
	private InputStream is;
	private BufferedInputStream bis;
	private ObjectInputStream ois;
	private int port;
	private DBStoreImpl dbstore;
	private LoggerImpl logger;
	private BackUpImpl back;
	private Configuration configuration;
	
//	private static Logger1 logger = new LoggerImpl();
//	private BackUp back = new BackUpImpl();
	
	@Override
	public void init(Properties properties) throws Exception {
		 port=Integer.parseInt(properties.getProperty("port"));
	}
	
	@Override
	public void setConfiguration(Configuration configuration) {
        this.configuration=configuration;
        try{
            dbstore=(DBStoreImpl) configuration.getDBStore();
            logger=(LoggerImpl)configuration.getLogger();
            back=(BackUpImpl)configuration.getBackup();
        }catch(Exception e){
            e.printStackTrace();
        }
	}
	 
	@Override
	public void shutdown() throws Exception {
		if(ois!=null)
			ois.close();
		if(bis!=null)
			bis.close();
		if(is!=null)
			is.close();
		if(socket!=null)
			socket.close();
		if(ss!=null)
			ss.close();
		//System.out.println("发生异常，服务器关闭！");
		logger.warn("发生异常，服务器关闭！");
		
	}

	@Override
	public void revicer() throws Exception {
		//System.out.println("服务器已启动，等待客户端连接！");
		logger.info("服务器已启动，等待客户端连接！");
		ss = new ServerSocket(port);
		while (true) {
			try {
				socket = ss.accept();
				//System.out.println("连接成功");
				logger.info("连接成功");
				new DBThread(socket,logger,back,dbstore).start();
			} catch (Exception e) {
				shutdown();
			}
			
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigurationImpl conf = new ConfigurationImpl();
		ServerImpl1 serverImpl1 = (ServerImpl1) conf.getServer();
		serverImpl1.revicer();
		System.out.println("入库完毕！");
	}


}

class DBThread extends Thread{
	private InputStream is=null;
	private BufferedInputStream bis=null;
	private ObjectInputStream ois=null;
	private Socket socket;
	private Logger1 logger;
	private BackUp back;
	private DBStore dbStore;
	private Configuration configuration=new ConfigurationImpl();
	
	public DBThread(Socket socket,Logger1 logger,BackUp back,DBStore dbStore) {
		this.socket = socket;
		this.logger=logger;
		this.back=back;
		this.dbStore=dbStore;
	}
	
	public DBThread(Socket socket,DBStore dbStore) {
		this.socket = socket;
		try {
			this.logger=configuration.getLogger();
			this.back=configuration.getBackup();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.dbStore=dbStore;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			String addresString=InetAddress.getLocalHost().getHostAddress();
			//System.out.println("客户端ip："+addresString);
			logger.debug("客户端ip："+addresString);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			is = socket.getInputStream();
			bis = new BufferedInputStream(is);
			ois = new ObjectInputStream(bis);
			Collection<Environment> collection = (Collection<Environment>) ois.readObject();
//			System.out.println("服务器接收的数据行："+collection.size());
//			System.out.println("准备将数据存入数据库");
			logger.info("服务器接收的数据行："+collection.size());
			logger.info("准备将数据存入数据库");
			// 加载备份
	        //把之前入库失败并保存在serverBack文件中的数据，读到类中（在读取的同时，保存备份文件）
	        Object obj=back.load("serverBack",BackUp.LOAD_UNREMOVE);//BackUP.LOAD_UNREMOVE=false
	        if(obj!=null){
	            logger.warn("服务器正在加载备份数据");
	            Collection<Environment> col2=(Collection<Environment>)obj;
	            collection.addAll(col2);
	            logger.warn("备份数据加载完成");
	        } 
	        try {
	        	dbStore.saveDB(collection);
			} catch (Exception e) {
				// 备份
	            logger.error("发生错误，服务器正在备份数据");
	            try{
	                // 把入库失败的数据备份到serverBack文件（在保存的同时覆盖原来的数据）
	                back.store("serverBack",collection,BackUp.STORE_OVERRIDE);
	                logger.warn("数据备份成功");
	            }catch(Exception e2){
	                logger.error("数据备份失败");
	            }
			}
//			System.out.println("存入数据库成功，存入数据行数："+collection.size());
			logger.info("存入数据库成功，存入数据行数"+collection.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
