package com.briup.environment.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.swing.border.EmptyBorder;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackUp;
import com.briup.environment.util.BackUpImpl;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationImpl;
import com.briup.environment.util.LogImpl;
import com.briup.environment.util.Logger1;
import com.briup.environment.util.LoggerImpl;

@SuppressWarnings({ "resource", "unused" })
public class GatherImpl implements Gather {

	//Environment对象集合用来保存获取的对象数据
	Collection<Environment> collection= new ArrayList<Environment>();
	//采集的原始文件路径
	//private String path="src/radwtmp";
	//保存上一次读取的字节数的文件
	//private String path2="src/record";
	
	private String path;
	private String path2;
	private LogImpl log =new LogImpl();
	private Logger1 logger;
	private BackUpImpl back;
	private Configuration configuration;
	
	//老师的方法，不需要setConfiguration()方法
//	private static Configuration configuration=new ConfigurationImpl();
//	private static Logger1 logger;
//	private BackUp back;  
	
	@Override
	public void init(Properties properties) throws Exception {
		path=properties.getProperty("src-file");
		path2=properties.getProperty("record-file");
	}
	
	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration=configuration;
		try {
			logger=(LoggerImpl) configuration.getLogger();
			back=(BackUpImpl) configuration.getBackup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Environment> gather() throws Exception {
		//获取日志配置文件中的filepath路径是在静态代码块中时
//		logger =configuration.getLogger();
//		back = configuration.getBackup();
		
		/*
		 * 1.从path2指定的路径读取保存的上一次读取的字节数的文件，第一次这个文件不存在，需要分情况判断
		 * 2.读取radwtmp文件的有效字节数，将返回值保存到path2指定的路径文件中
		 * 3.先略过上一次读取的字节数，再进行本次解析
		 */
		File file = new File(path2);
		
		long num=0;
		if(file.exists()){
			DataInputStream dis = 
					new DataInputStream(new FileInputStream(file));
			num=dis.readLong();
		}
		/*
		 * 创建RandomAccessFile流实现略过功能
		 * 创建时提供两个参数，第一个参数是表示读取文件，第二个参数表示以什么形式读取，r代表只读
		 */
	
		RandomAccessFile raf = new RandomAccessFile(path, "r");
		
		long num2=raf.length();
		//略过path2中的字节数长度
		raf.seek(num);
		DataOutputStream dos = 
				new DataOutputStream(new FileOutputStream(path2));
		dos.writeLong(num2);
		
		/*
		 * 1.构建缓存字符流按行读取 BufferReader
		 * 2.根据|分割字符串，根据第四个字段的不同分别赋予温度，湿度，二氧化碳，光照强度的环境名称
		 * 3.第七个字段表示16进制环境值，将16进制转化为10进制，
		 * 	如果是温度和湿度数据，前两个字节是温度数据，中间两个字节是湿度数据，
		 * 	如果是二氧化碳和光照强度，前两个字节就是对于的数据
		 * 4.温度和湿度是同一条记录，读取一行需要创建两个Environment对象，并且分别赋予值
		 * 5.封装好的对象添加到Coll集合中
		 */
		String s=null;
		Environment environment=null;
		//统计对象个数
		//count1统计温度和湿度条数（str[3]=16）
		int count1=0;
		//count2统计光照强度条数（str[3]=256）
		int count2=0;
		//count3统计二氧化碳条数（str[3]=1280）
		int count3=0;
   
        //把之前发送失败并保存在clientBack文件中的数据，读到类中（在读取的同时，保存备份文件）
        Object obj = back.load("clientBack", BackUp.LOAD_UNREMOVE);//BackUP.LOAD_UNREMOVE=false
        if(obj!=null){
            //将未匹配成功的数据加到本次Collection集合中 用io流将采集模块的异常数据写入备份文件中（异常数据保存在collection集合中）
            collection.addAll((Collection<Environment>) obj);
        }
       // 将上次读取的字节数读取出来
        Long skip=(Long) back.load("skip",BackUp.LOAD_REMOVE);
        if(skip!=null){
            raf.seek(skip);
        }else{
            skip=0L;
        }
        //还原
        Collection<Environment> environments=
        		(Collection<Environment>) back.load("EnvironmentFile",BackUp.LOAD_REMOVE);
        if(environments!=null){
            logger.warn("还原备份数据");
            collection.addAll(environments);
        }
		logger.info("读取数据");
		while ((s=raf.readLine())!=null) {
			skip+=s.length()+2; //windows下回车占两个字符
			environment=new Environment();
			String[] str = s.split("[ | ]");
			environment.setSrcId(str[0]);
			environment.setDesId(str[1]);
			environment.setDevId(str[2]);
			environment.setSersorAddress(str[3]);
			environment.setCount(Integer.parseInt(str[4]));
			environment.setCmd(str[5]);
			environment.setStatus(Integer.parseInt(str[7]));
			//environment.setGather_date(Timestamp.valueOf(str[8]));
			Long time = new Long(str[8]);
			Timestamp timestamp = new Timestamp(time);
			environment.setGather_date(timestamp);
		
			if (str[3].equals("16")) {
				//s.substring(0,4)左闭右开
				//根据温度转换公式将温度值从16进制转化为10进制
				float value=(float)((Integer.parseInt(str[6].substring(0,4),16)*0.00268127)-46.85);
				environment.setName("温度");
				environment.setData(value);
				collection.add(environment);            
				
				//新建一个environment对象
				environment = new Environment();
				environment.setSrcId(str[0]);
				environment.setDesId(str[1]);
				environment.setDevId(str[2]);
				environment.setSersorAddress(str[3]);
				environment.setCount(Integer.parseInt(str[4]));
				environment.setCmd(str[5]);
				environment.setStatus(Integer.parseInt(str[7]));
				environment.setGather_date(timestamp); 
				
				/*Environment environment2=new Environment();
				environment2 = environment;
				float value2=(float)((Integer.parseInt(str[6].substring(4,8),16)*0.00190735)-6);
				environment2.setName("湿度");
				environment2.setData(value2);
				collection.add(environment2);*/
				
				float value2=(float)((Integer.parseInt(str[6].substring(4,8),16)*0.00190735)-6);
				environment.setName("湿度");
				environment.setData(value2);
				collection.add(environment);
				count1++;
			}else {
				//求取光照强度和二氧化碳
				float value = Integer.valueOf(str[6].substring(0,4),16);
				if (str[3].equals("256")){
					environment.setName("光照强度");
					environment.setData(value);
					collection.add(environment);
					count2++;
				}
				if (str[3].equals("1280")){
					environment.setName("二氧化碳");
					environment.setData(value);
					collection.add(environment);
					count3++;
				}
			}                           		
		}
		logger.debug("采集环境数据："+collection.size());
		 log.info("采集环境数据："+collection.size());
		/*System.out.println("采集环境数据："+collection.size());
		System.out.println("温度、湿度："+count1);
		System.out.println("光照强度："+count2);
		System.out.println("二氧化碳："+count3);*/
		
		 // 备份
        log.warn("备份数据");
        logger.warn("备份数据");
        back.store("EnvironmentFile",environments,BackUp.STORE_OVERRIDE);
        back.store("skip",skip,BackUp.STORE_OVERRIDE);
        
		logger.info("温度、湿度："+count1);
		logger.info("光照强度："+count2);
		logger.info("二氧化碳："+count3);
		return collection;
	}

	public static void main(String[] args) throws Exception {
		ConfigurationImpl conf = new ConfigurationImpl();
		Gather gather = conf.getGather();
		gather.gather();
	}

	
}
