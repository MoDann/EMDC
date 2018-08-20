package com.briup.environment.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 数据库连接工具
 * @author ASUS
 *
 */
public class ConnectionFactory {

	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static Properties properties;
	private static Connection connection;
	
	static{
		properties = new Properties();
		InputStream is;
		try {
			is = new FileInputStream(new File("src/jdbc.properties"));
			//load加载需要读取的文件
			properties.load(is);
			driver=properties.getProperty("driver");
			url=properties.getProperty("url");
			username=properties.getProperty("user");
			password=properties.getProperty("password");
		//	System.out.println(driver+" "+url+" "+username+" "+password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
			Class.forName(driver);
			connection=DriverManager.getConnection(url,username,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
}
