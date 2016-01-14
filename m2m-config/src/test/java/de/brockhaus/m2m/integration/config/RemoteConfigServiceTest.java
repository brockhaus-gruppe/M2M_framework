package de.brockhaus.m2m.integration.config;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.config.ConfigurationChangeListener;
import de.brockhaus.m2m.config.ConfigurationService;
import de.brockhaus.m2m.message.M2MDataType;

/**
 * Please note that UnicastRemoteObject has to be extended because of the callback
 *
 * Project: m2m-config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 1, 2015
 *
 */
public class RemoteConfigServiceTest extends UnicastRemoteObject implements ConfigurationChangeListener {
	
	protected RemoteConfigServiceTest() throws RemoteException {
		super();
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		RemoteConfigServiceTest test = new RemoteConfigServiceTest();
		test.testSaveConfiguration();
	}
	
	public void testSaveConfiguration() throws RemoteException, MalformedURLException, NotBoundException {
		
		// using RMI
		ConfigurationService service = (ConfigurationService) Naming.lookup("rmi://localhost:1399/config_service");
		// registering for notification in the case of a config change
		service.registerForChanges(this);
		
		Configuration config = service.getConfig();
		
		HashMap<String,String> values = new HashMap<String, String>();
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2251", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2253", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2254", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_FA011.AA.R244", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_FA011.AA.R244", M2MDataType.FLOAT.toString());
		config.setConfigForElement("sensors", values);
		
		service.setConfig(config);	
	}

	public void onConfigurationChange() {
		System.out.println("configuration has changed");
	}
}
