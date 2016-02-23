package de.brockhaus.m2m.integration.config;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.jboss.weld.environment.se.Weld;

import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.config.ConfigurationChangeListenerLocal;
import de.brockhaus.m2m.config.ConfigurationServiceLocal;
import de.brockhaus.m2m.integration.config.c8y.C8YSensorMapping;

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
	
	private HashMap<String, String> devices;
	private HashMap<String, String> sensors;
	private HashMap<String, String> credentials;
	private ConfigurationServiceLocal service;
	
	public static void main(String[] args) throws RemoteException {
		ConfigServiceTest test = new ConfigServiceTest();
		test.testSaveConfiguration();
	}

	public ConfigServiceTest() {
		this.init();
	}
	
	public void testSaveConfiguration() throws RemoteException {

		Configuration config = service.getConfig();
		config.setConfigForElement("devices", devices);
		config.setConfigForElement("sensors", sensors);
		config.setConfigForElement("credentials", credentials);
		
		service.setConfig(config);
	}

	public void init() {

		// using WELD container
		service = new Weld().initialize().instance().select(ConfigurationServiceImpl.class).get();
		service.registerForChangesLocally(this);
		
		// devices
		devices = new HashMap<String, String>();
		devices.put("device_gid", "11654");
		
		// mapping sensors to data types 
		sensors = new HashMap<String, String>();
		sensors.put(
				"0",  
				new C8YSensorMapping(0, "Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor conveyer belt swap", "10991", "11654").toString()
				);	
		sensors.put(
				"1",  
				new C8YSensorMapping(1, "Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor drilling machine", "10991", "11654").toString()
				);
		sensors.put(
				"2",  
				new C8YSensorMapping(2, "Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor loading station", "10991", "11654").toString()
				);
		sensors.put(
				"3",  
				new C8YSensorMapping(3, "Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor milling machine", "10991" , "11654").toString()
				);
		sensors.put(
				"4",  
				new C8YSensorMapping(4, "Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor slider 1", "10991", "11654").toString()
				);
		sensors.put(
				"5",  
				new C8YSensorMapping(5, "Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 1 front", "10991", "11654").toString()
				);
		sensors.put(
				"6",  
				new C8YSensorMapping(6, "Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 1 rear", "10991", "11654").toString()
				);
		sensors.put(
				"7",  
				new C8YSensorMapping(7, "Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 2 front", "10991", "11654").toString()
				);
		sensors.put(
				"8",  
				new C8YSensorMapping(8, "Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 2 rear", "10991", "11654").toString()
				);
		
		// credentials
		credentials = new HashMap<String, String>();
		credentials.put("user", "jperez@brockhaus-gruppe.de");
		credentials.put("pwd", "brockhaus");
		credentials.put("url", "https://brockhaus.cumulocity.com");
	}
	
	public void onConfigurationChange() {
		System.out.println("configuration has changed");
	}
}
