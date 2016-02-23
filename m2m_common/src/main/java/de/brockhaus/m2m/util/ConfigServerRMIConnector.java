package de.brockhaus.m2m.util;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Properties;

import de.brockhaus.m2m.config.ConfigurationService;

/**
 * The central connector to the config service. For configuration see config.properties
 * 
 * Project: integration_common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 18, 2015
 *
 */
public class ConfigServerRMIConnector {
	
	private ConfigServerRMIConnector() {
		// you have to use the static method
	}
	
	public static ConfigurationService doConnect() {
		ConfigurationService configService = null;

		//FIXME leads to cyclic dependencies
//		try {
//			configService = new ConfigurationServiceImpl();
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		return ConfigServerRMIConnector.doConnectRemotely();
	}

	public static ConfigurationService doConnectRemotely() {
		Properties props = new Properties();
		ConfigurationService configService = null;
		try {
			props.load(ConfigServerRMIConnector.class.getClassLoader().getResourceAsStream("rmiconfig.properties"));
			configService = (ConfigurationService) Naming.lookup("rmi://" 
					+ props.getProperty("confighost") +":" 
					+ props.getProperty("configport") + "/"
					+ props.getProperty("configname"));
		} catch (IOException | NotBoundException e) {
			e.printStackTrace();
		}
		return configService;
	}
}
