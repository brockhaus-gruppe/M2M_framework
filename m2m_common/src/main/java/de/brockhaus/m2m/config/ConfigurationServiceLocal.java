package de.brockhaus.m2m.config;

import java.rmi.RemoteException;


/**
 * 
 * The local interface for the configuration service 
 * 
 * Project: integration_common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 22, 2015
 *
 */
public interface ConfigurationServiceLocal {

	public Configuration getConfig();
	public void setConfig(Configuration config);
	public void updateConfig(Configuration config);
	public void registerForChangesLocally(ConfigurationChangeListenerLocal listener);
}
