package com.briup.environment.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.server.Server1;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationImpl;

public class EmdcTest {

	private Configuration configuration;
	//相当于静态代码块
	@Before
	public void setUp() throws Exception {
		configuration = new ConfigurationImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void client_test() throws Exception{
		Gather gather = configuration.getGather();
		Client client = configuration.getClient();
		client.send(gather.gather());
	}
	@Test
	public void server_test() throws Exception{
		Server1 server = configuration.getServer();
		server.revicer();
	}
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
