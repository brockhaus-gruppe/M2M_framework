package de.brockhaus.m2m.integration.config;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.jboss.weld.environment.se.Weld;

import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.config.ConfigurationChangeListenerLocal;
import de.brockhaus.m2m.config.ConfigurationServiceLocal;
import de.brockhaus.m2m.message.M2MDataType;

/**
 * 
 *
 * Project: m2m-config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 1, 2015
 *
 */
public class ConfigServiceTest implements ConfigurationChangeListenerLocal {

	public static void main(String[] args) throws RemoteException {
		ConfigServiceTest test = new ConfigServiceTest();
		test.testSaveConfiguration();
	}

	private HashMap<String, String> values;
	private ConfigurationServiceLocal service;
	private HashMap<String, String> ttl;

	public ConfigServiceTest() {
		this.init();
	}
	
	public void testSaveConfiguration() throws RemoteException {

		Configuration config = service.getConfig();
		config.setConfigForElement("sensors", values);
		config.setConfigForElement("sensor_ttl", ttl);

		service.setConfig(config);
	}

	public void init() {

		// using WELD container
		service = new Weld().initialize().instance()
				.select(ConfigurationServiceImpl.class).get();
		service.registerForChangesLocally(this);
		
		// mapping sensors to data types 
		values = new HashMap<String, String>();
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2251",
				M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2253",
				M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2254",
				M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_FA011.AA.R244",
				M2MDataType.FLOAT.toString());
		
		// mapping sensor data and time to live
		//TTL = 1 week = 604800000 ms
		ttl = new HashMap<String, String>();
		ttl.put("PT_DS1_316233.ED01_AB219_M04.AS.V2251",
				"604800000");
		ttl.put("PT_DS1_316233.ED01_AB219_M04.AS.V2253",
				"604800000");
		ttl.put("PT_DS1_316233.ED01_AB219_M04.AS.V2254",
				"604800000");
		ttl.put("PT_DS1_316233.ED01_FA011.AA.R244",
				"604800000");

	}

	public void onConfigurationChange() {
		System.out.println("configuration has changed");
	}
}
