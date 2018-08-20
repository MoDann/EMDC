package com.briup.environment.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;



import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.gui.Login;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server1;

public class ConfigurationImplDom4j implements Configuration {

	private Map<String, EmdcModule> map=new HashMap<String, EmdcModule>();
	
	public ConfigurationImplDom4j() {
		this("src/config.xml");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ConfigurationImplDom4j(String conf) {
		//1.构建SAXReader对象
      	SAXReader reader = new SAXReader();
      	try {
      		//2.获取文档对象
			Document document = reader.read(conf);
			//3.获取根节点
			Element root = document.getRootElement();
			System.out.println("根节点："+root);
			//4.获取根节点下的所有子节点
			Iterator<Element> it1 = root.elementIterator();
			while (it1.hasNext()) {
				Element element1 = it1.next();
				String tagName = element1.getName();
				String tagValue = element1.attributeValue("class");
				System.out.println(tagName+"="+tagValue);
				EmdcModule emdcModule = (EmdcModule) Class.forName(tagValue).newInstance();
				map.put(tagName, emdcModule);
				Properties properties = new Properties();
				List elements = element1.elements();
				for (Object object : elements) {
					Element element2 = (Element) object;
					String nodeName = element2.getName();
					String nodeValue = element2.getText();
					properties.put(nodeName, nodeValue);
				}
				emdcModule.init(properties);
				for(EmdcModule module:map.values())
					module.setConfiguration(this);
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
		return null;
	}

	public static void main(String[] args) {
		ConfigurationImplDom4j dom4j = new ConfigurationImplDom4j();
		try {
			System.out.println(dom4j.getGather());
			System.out.println(dom4j.getClient());
			System.out.println(dom4j.getServer());
			System.out.println(dom4j.getDBStore());
			System.out.println(dom4j.getBackup());
			System.out.println(dom4j.getLogger());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
