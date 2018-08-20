package com.briup.environment.util;

import java.io.File;
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

public class ConfigurationImpl1 implements Configuration {

	/*
	 * 读取config.xml内容，将标签作为key，累得实例对象作为值存储到map集合中
	 */
	private Map<String, EmdcModule> map=new HashMap<String, EmdcModule>();
	
	public ConfigurationImpl1() {
		this("src/config.xml");
	}
	//
	public ConfigurationImpl1(String config) {
		//构建解析工厂，抽象方法，不能直接new
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			//构建解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(config);
			//处理解析文件，将给定文件的内容解析为一个 XML 文档，并且返回一个新的 DOM Document 对象。
			Document document = builder.parse(file);
			/*
			 * 1.获取根节点
			 * 2.获取根节点之后的所有子节点NodeList
			 * 3.遍历子节点，根据节点类型获取元素节点
			 * 4.遍历元素节点，获取节点名称和属性节点的值
			 * 5.将class属性节点的值进行实例化
			 * 6.map按照元素节点名称为key，实例化对象为value进行保存
			 */
			Element e = document.getDocumentElement();
			NodeList nl1 = e.getChildNodes();
			for (int i = 0; i < nl1.getLength(); i++) {
				if (nl1.item(i).getNodeType()==1) {
					Element element1=(Element) nl1.item(i);
					String tagName = element1.getNodeName();
					String attValue = element1.getAttribute("class");
					System.out.println(tagName+"="+attValue);
					EmdcModule emdcModule=(EmdcModule)Class.forName(attValue).newInstance();
					map.put(tagName, emdcModule);
					/*
					 * 1.获取当前节点的子节点，构建Properties对象
					 * 2.根据节点类型判断获取元素节点
					 * 3.取元素节点的名称为key，文本节点值为value
					 * 4.properties按照节点名称为key和文本节点为value进行保存
					 * 5.EmdcModule调用init方法保存properties对象
					 */
					NodeList nl2 = nl1.item(i).getChildNodes();
					Properties properties = new Properties();
					for (int j = 0; j < nl2.getLength(); j++) {
						if (nl2.item(j).getNodeType()==1) {
							Element element2 = (Element) nl2.item(j);
							String nodeName = element2.getNodeName();
							String nodeValue = element2.getTextContent();
							System.out.println(nodeName+"="+nodeValue);
							properties.put(nodeName, nodeValue);
						}
					}
					emdcModule.init(properties);
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
		return null;
	}

	public static void main(String[] args) {
		ConfigurationImpl1 configuration = new ConfigurationImpl1();
		try {
			System.out.println(configuration.getGather());
			System.out.println(configuration.getClient());
			System.out.println(configuration.getServer());
			System.out.println(configuration.getDBStore());
			System.out.println(configuration.getLogger());
			System.out.println(configuration.getBackup());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
