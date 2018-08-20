package com.briup.environment.client;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackUp;
import com.briup.environment.util.BackUpImpl;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationImpl;
import com.briup.environment.util.Logger1;
import com.briup.environment.util.LoggerImpl;


public class ClientImpl implements Client{

	
//	private String server_ip="127.0.0.1";
//	private int port=9999;
	//private LogImpl log = new LogImpl();
	private String server_ip;
	private int port;
//	private static Logger1 logger=new LoggerImpl();
//	private BackUp back = new BackUpImpl();
	private static LoggerImpl logger;
	private BackUpImpl back;
	private Configuration configuration;
	
	@Override
	public void init(Properties properties) throws Exception {
		server_ip=properties.getProperty("server-ip");
        port = Integer.parseInt(properties.getProperty("port"));
	}
	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration=configuration;
		try {
			logger=(LoggerImpl) configuration.getLogger();
			back=(BackUpImpl) configuration.getBackup();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void send(Collection<Environment> coll) throws Exception {
		try {
			 //把之前发送失败并保存在clientBack文件中的数据，读到类中（在读取的同时，保存备份文件）
            Collection<Environment> col =
            		(Collection<Environment>) back.load("clientBack", BackUp.LOAD_UNREMOVE);//BackUP.LOAD_UNREMOVE=false
            if (col != null) {
                logger.warn("找到备份");
                coll.addAll(col);
                logger.warn("装载备份");
            }
          //1.构建Socket对象，获取服务器连接信息
    		//log.info("正在连接服务器！");
    		logger.info("正在连接服务器！");
    		//System.out.println("正在连接服务器！");
    		Socket socket = new Socket(server_ip, port);
    		//log.info("服务器连接成功！准备发送数据！");
    		logger.info("服务器连接成功！准备发送数据！");
    		//System.out.println("服务器连接成功！准备发送数据！");
    		/*
    		 * 客户端发送数据，通过Socket创建输出流，由于发送采集模块采集好的environment集合对象，所以封装成Object输出流
    		 * 2.获取网络输出流
    		 * 3.封装输出流
    		 */
    		// ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
    		OutputStream os = socket.getOutputStream();
    		BufferedOutputStream bos = new BufferedOutputStream(os);
    		ObjectOutputStream oos = new ObjectOutputStream(bos);
    		//4.发送服务器
    		//log.info("正在发生数据！");
    		logger.info("正在发生数据！");
    		oos.writeObject(coll);
    		oos.flush();
    		//log.info("发送成功！发送数据行数："+coll.size());
    		logger.info("发送成功！发送数据行数："+coll.size());
    		//System.out.println("发送成功！发送数据行数："+coll.size());
    		oos.close();
    		bos.close();
    		os.close();
    		socket.close();
		} catch (Exception e) {
			 // 备份
            logger.error("正在备份数据！");
            try{
                // 把发送失败的数据备份到clientBack文件（在保存的同时覆盖原来的数据）
                back.store("clientBack",coll,BackUp.STORE_OVERRIDE);
                logger.warn("备份结束!");
            }catch(Exception e2){
                logger.error("客户端备份数据错误");
            }

		}
		
	}

	public static void main(String[] args) throws Exception {
		ConfigurationImpl conf = new ConfigurationImpl();
		Gather gather2 = conf.getGather();
		ClientImpl client = (ClientImpl) conf.getClient();
		Collection<Environment> gather = gather2.gather();
		client.send(gather);
		System.out.println("客户端发送完毕！");
	}

	
}
