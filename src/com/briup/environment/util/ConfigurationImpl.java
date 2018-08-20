package com.briup.environment.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.gui.Login;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server1;

/**
 * 配置模块
 * @author ASUS
 *
 */
public class ConfigurationImpl implements Configuration {

	private Map<String, EmdcModule> map=new HashMap<String, EmdcModule>();
	
	public ConfigurationImpl() {
		this("src/emdc.xml");
	}

	//解析配置文件(Dom解析方式)
	public ConfigurationImpl(String conf) {
		//构建解析工厂，抽象方法，不能直接new
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			//构建解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			//处理解析文件，将给定文件的内容解析为一个 XML 文档，并且返回一个新的 DOM Document 对象。
			Document document = builder.parse(conf);
			//按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList。
			NodeList nl1 = document.getElementsByTagName("emdc");
			for (int i = 0; i < nl1.getLength(); i++) {
				Element element = (Element) nl1.item(i);
				System.out.println("根目录："+element.getTagName());
				NodeList nl2 = nl1.item(i).getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					if (nl2.item(j).getNodeType()==Node.ELEMENT_NODE) {//判断是否为元素节点
						Element element2 = (Element) nl2.item(j);
						String className = element2.getAttribute("class");
						System.out.println("第一级子目录："+element2.getTagName());
						EmdcModule emdcModule = (EmdcModule) Class.forName(className).newInstance();
						Properties properties = new Properties();
						NodeList nl3 = nl2.item(j).getChildNodes();
						for (int k = 0; k < nl3.getLength(); k++) {
							if (nl3.item(k).getNodeType()==Node.ELEMENT_NODE) {
								Element element3 = (Element) nl3.item(k);
								String nodeName = element3.getNodeName();
								String string = element3.getTextContent();
								System.out.println("二级子目录："+nodeName);
								//将解析好的配置信息以键值对的方式保存在properties对象中
								//properties.put(nodeName, string);
								properties.setProperty(nodeName, string);//只允许String
							}
							
						}
						 //通过反射得到每个模块的实例，并且调用它们的init（）方法进行动态传值
						emdcModule.init(properties);
						map.put(element2.getNodeName(), emdcModule);
						  //把自己(配置模块本身)注入到需要的模块中
						for(Object object:map.values()){
		                    if(object instanceof ConfigurationAWare){
		                        ((ConfigurationAWare) object).setConfiguration(this);
		                    }
		                }
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Logger1 getLogger() throws Exception {
		return (Logger1) map.get("log");
	}

	@Override
	public BackUp getBackup() throws Exception {
		return (BackUp) map.get("backup");
	}

	@Override
	public Gather getGather() throws Exception {
		return (Gather) map.get("gather");
	}

	@Override
	public Client getClient() throws Exception {
		return (Client) map.get("client");
	}

	@Override
	public Server1 getServer() throws Exception {
		return (Server1) map.get("server");
	}

	@Override
	public DBStore getDBStore() throws Exception {
		return (DBStore) map.get("dbstore");
	}

	@Override
	public Login getLogin() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		 ConfigurationImpl configuration=new ConfigurationImpl();
	     System.out.println(configuration.getGather());
	     System.out.println(configuration.getClient());
	     System.out.println(configuration.getServer());
	     System.out.println(configuration.getDBStore());
	     System.out.println(configuration.getLogger());
	     System.out.println(configuration.getBackup());     
	}

}
