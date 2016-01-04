package de.brockhaus.m2m.config;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface for the configuration service
 * 
 * Project: integration_config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 17, 2015
 *
 */
public interface ConfigurationService extends Remote {

	public Configuration getConfig() throws RemoteException;
	public void setConfig(Configuration config) throws RemoteException;
	public void updateConfig(Configuration config) throws RemoteException;
	
	public void registerForChanges(ConfigurationChangeListener listener) throws RemoteException;
}
