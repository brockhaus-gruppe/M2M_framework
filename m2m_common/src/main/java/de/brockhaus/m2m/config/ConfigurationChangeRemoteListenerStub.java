package de.brockhaus.m2m.config;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.util.ConfigServerRMIConnector;

/**
 * If to be notified by config changes, this class has to be used. The reason behind this is, that most
 * of the classes to be notified already extend AbstractM2MMessage handler and thus can't extend 
 * UnicastRemoteObject as well. Extending UnicastRemoteObject is a must in the field of RMI callbacks.
 * 
 * Include this class via Spring DI and everything will be done for you.
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jun 1, 2015
 *
 */
public class ConfigurationChangeRemoteListenerStub extends UnicastRemoteObject implements ConfigurationChangeListener {

	private static final Logger LOG = Logger.getLogger(ConfigurationChangeRemoteListenerStub.class);
	
	// whom to inform
	private ConfigurationChangeListener handler;
	
	private ConfigurationService configService;
	
	public ConfigurationChangeRemoteListenerStub() throws RemoteException {
		
	}

	@Override
	public void onConfigurationChange() throws RemoteException {
		this.handler.onConfigurationChange();
	}
	
	private void init() {
		this.configService = ConfigServerRMIConnector.doConnect();
	}

	public ConfigurationChangeListener getHandler() {
		return handler;
	}

	public void setHandler(ConfigurationChangeListener handler) throws RemoteException {
		this.handler = handler;
		this.configService.registerForChanges(this);
	}

	public ConfigurationService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigurationService configService) {
		this.configService = configService;
	}

}
