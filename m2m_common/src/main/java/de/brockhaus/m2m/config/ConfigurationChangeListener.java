package de.brockhaus.m2m.config;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote version of being informed about a config change ...
 * 
 * TODO implement RMI callbacks
 * 
 * Project: integration_common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 22, 2015
 *
 */
public interface ConfigurationChangeListener extends Remote, Serializable {

	void onConfigurationChange() throws RemoteException;

}
