package com.briup.environment.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackUpImpl;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.LogImpl;
import com.briup.environment.util.LoggerImpl;

public class DBStoreImpl1 implements DBStore{
//	private String driver="oracle.jdbc.driver.OracleDriver";
//	private String url="jdbc:oracle:thin:@localhost:1521:XE";
//	private String username="envir";
//	private String password="envir";
	private Connection connection;
	private PreparedStatement pstate;
//	private int batchsize=500;
//	private static LoggerImpl logger = new LoggerImpl();
	
	private String driver;
    private String url;
    private String username;
    private String password;
    private int batchsize;
    private Configuration conf;
    private LoggerImpl logger;
    private BackUpImpl back;
	

	@Override
	public void init(Properties properties) throws Exception {
		driver=properties.getProperty("driver");
        url=properties.getProperty("url");
        username=properties.getProperty("username");
        password=properties.getProperty("password");
     //   batchsize=Integer.parseInt(properties.getProperty("batch-size"));
	}
	
	
	@Override
	public void setConfiguration(Configuration configuration) {
		this.conf=configuration;
	    try {
	    	logger=(LoggerImpl) configuration.getLogger();
	        back=(BackUpImpl)configuration.getBackup();
	     } catch (Exception e) {
	    	e.printStackTrace();
	     }
		
	}


	@Override
	public void saveDB(Collection<Environment> c) throws Exception {
		Class.forName(driver);
		connection = DriverManager.getConnection(url,username,password);
		//表示当前批处理中的sql语句数
		int count=0;
		//记录当前天数
		int day=0;
		for (Environment environment : c) {
			/*
			 * 在两种情况下，需要创建新的ps对象
			 * 1.ps==null第一次进入for循环中
			 * 2.当日期发生变化的时候，day！=当前对象采集时间
			 * environment.getGather_date()返回类型为Timestamp，
			 * Timestamp对象的getDate()返回day of month，
			 * Timestamp对象的getDay()返回0-6对应的星期天—星期六
			 */
			if(pstate==null || day!=environment.getGather_date().getDate()){
				day=environment.getGather_date().getDate();
				//System.out.println("数据库入库天数："+day);
				logger.debug("数据库入库天数："+day);
				/*
				 * 日期发生变化1号变为2号为防止1号中存在没有处理的sql语句，
				 * 举例：批处理缓存中有500条记录，1号数据有300条，2号数据有200条
				 * 需要分开插入不同的数据库表，由于此时没有达到缓存大小，需要手动提交数据
				 */
				if(pstate!=null){
					//处理前一天留下来的sql语句
					pstate.executeBatch();
					//清空缓存
					pstate.clearBatch();
					//关闭ps，构建新的ps对象
					pstate.cancel();
				}
				String sql = "insert into e_detail_"+day+" values(?,?,?,?,?,?,?,?,?)";
				pstate = connection.prepareStatement(sql);
			}
			pstate.setString(1, environment.getName());
			pstate.setString(2, environment.getSrcId());
			pstate.setString(3, environment.getDesId());
			pstate.setString(4, environment.getSersorAddress());
			pstate.setInt(5, environment.getCount());
			pstate.setString(6, environment.getCmd());
			pstate.setInt(7, environment.getStatus());
			pstate.setFloat(8, environment.getData());
			pstate.setTimestamp(9, environment.getGather_date());
			//将sql语句放入批处理中
			pstate.addBatch();
			/*
			 * 记录当前批处理中的sql语句数量，当满足批处理条数要求时提交，
			 * 或者for循环中存在处理完毕的sql语句，也就是在for循环结束时，批处理中仍然有处理的sql语句同样提交
			 */
			count++;
			if(count%batchsize==0){
				pstate.executeBatch();
				pstate.clearBatch();
			}
			
		}
		if(pstate!=null){
			pstate.executeBatch();
			pstate.clearBatch();
			pstate.close();
		}
		
	}

}
