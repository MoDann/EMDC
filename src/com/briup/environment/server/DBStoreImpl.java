package com.briup.environment.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.client.GatherImpl;
import com.briup.environment.util.BackUp;
import com.briup.environment.util.BackUpImpl;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConnectionFactory;
import com.briup.environment.util.Log;
import com.briup.environment.util.LogImpl;
import com.briup.environment.util.LoggerImpl;

public class DBStoreImpl implements DBStore{

	private Connection connection;
	private PreparedStatement pstate;
//	private int bacthsize=500;
//	private Log logger = z;
//	private BackUp back = new BackUpImpl();
	
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
        batchsize=Integer.parseInt(properties.getProperty("batch-size"));
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveDB(Collection<Environment> c) throws Exception{
		try {
			 Collection<Environment> collection = 
					 (Collection<Environment>) back.load("dbstoreBack", BackUp.LOAD_UNREMOVE);
	          if (collection != null) {
	              logger.info("正在装载备份数据");
	              c.addAll(collection);
	              logger.info("备份数据装载完成");
	           }
	        Class.forName(driver);
	  		connection = DriverManager.getConnection(url,username,password);
//	        connection = ConnectionFactory.getConnection();
//	  		connection.setAutoCommit(false);
	  		//k为批处理标志，i为记录当前日期
	  		logger.info("等待存入数据库！");
	  		int k=0,i=0;
	  		for (Environment environment : c) {
	  			/*
	  			 * 在两种情况下，需要创建新的ps对象
	  			 * 1.ps==null第一次进入for循环中
	  			 * 2.当日期发生变化的时候，i！=当前对象采集时间（day）
	  			 * environment.getGather_date()返回类型为Timestamp，
	  			 * Timestamp对象的getDate()返回day of month，
	  			 * Timestamp对象的getDay()返回0-6对应的星期天—星期六
	  			 */
	  			int day=environment.getGather_date().getDate();
	  			if(day!=i){
	  				/*
	  				 * 日期发生变化1号变为2号为防止1号中存在没有处理的sql语句，
	  				 * 举例：批处理缓存中有500条记录，1号数据有300条，2号数据有200条
	  				 * 需要分开插入不同的数据库表，由于此时没有达到缓存大小，需要手动提交数据
	  				 */
	  				if(pstate!=null){
	  					//处理前一天留下来的sql语句
	  					pstate.executeBatch();
	  					pstate.close();
	  				}
	  				i=day;
	  				String sql="insert into e_detail_"+day+" values(?,?,?,?,?,?,?,?,?)";
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
	  			pstate.addBatch();
	  			if (((k++)%batchsize==0)||(k==c.size())) {
	  				pstate.executeBatch();
	  				pstate.clearBatch();
	  			//	connection.commit();
	  			}
	  		}
	  		logger.info("存储完毕！");
	  		logger.info("存储数据行数："+c.size());
	  		//System.out.println("存储完毕！");
	  		//System.out.println("存储数据行数："+c.size());
		} catch (Exception e) {
			// 备份
            logger.error("发生错误，数据回滚");
            back.store("dbstoreBack", c, BackUp.STORE_OVERRIDE);
            if (connection != null) {
                connection.rollback();
            }
		}
		if(pstate!=null)
	        pstate.close();
	    if(connection!=null)
	        connection.close();
	}
	
//	public static void main(String[] args) throws Exception {
//		GatherImpl gather = new GatherImpl();
//		DBStoreImpl dbStore = new DBStoreImpl();
//		dbStore.saveDB(gather.gather());
//	}


}
