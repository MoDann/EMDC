package com.briup.environment.server;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackUp;
import com.briup.environment.util.BackUpImpl;
import com.briup.environment.util.Log;
import com.briup.environment.util.LogImpl;

public class ServerImpl implements Server {

	private int port=9999;
	private ServerSocket ss=null;
	private Socket socket=null;
	private InputStream is=null;
	private BufferedInputStream bis=null;
	private ObjectInputStream ois=null;
	private Log logger = new LogImpl();
	private BackUp back = new BackUpImpl();
	                                 
//	@Override
//	public Collection<Environment> revicer() throws Exception  {
//		Handler handler = new ServerImpl().new Handler<>();
//		handler.start();
//		return handler.getCollection();
//	}
	@Override
	public void revicer() throws Exception {
		Handler handler = new ServerImpl().new Handler();
		handler.start();
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
		
	}
	
	//内部类声明为private，会隐藏实现方式
	private class Handler extends Thread{

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			try {
				ss = new ServerSocket(port);
				logger.info("服务器已启动，等待客户端连接！");
				//System.out.println("服务器已启动，等待客户端连接！");
				socket = ss.accept();
				logger.info("连接成功！");
				//System.out.println("连接成功！");
				is = socket.getInputStream();
				bis = new BufferedInputStream(is);
				ois = new ObjectInputStream(bis);
				Collection<Environment> collection = (Collection<Environment>) ois.readObject();
				logger.info("服务器接收的数据行："+collection.size());
				//System.out.println("服务器接收的数据行："+collection.size());
				logger.info("准备将数据存入数据库！");
				//System.out.println("准备将数据存入数据库");
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
		        	new DBStoreImpl().saveDB(collection);
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
				logger.info("存入数据库成功，存入数据行数："+collection.size());
				//System.out.println("存入数据库成功，存入数据行数："+collection.size());
				shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) throws Exception {
		new ServerImpl().revicer();
	}

}
