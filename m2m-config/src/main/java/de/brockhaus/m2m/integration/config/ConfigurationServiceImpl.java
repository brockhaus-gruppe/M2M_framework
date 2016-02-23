package de.brockhaus.m2m.integration.config;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.config.ConfigSerializer;
import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.config.ConfigurationChangeListener;
import de.brockhaus.m2m.config.ConfigurationChangeListenerLocal;
import de.brockhaus.m2m.config.ConfigurationService;
import de.brockhaus.m2m.config.ConfigurationServiceLocal;
import de.brockhaus.m2m.integration.annotation.JSONSerializer;

/**
 * 
 *
 * Project: m2m-config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Feb 22, 2016
 *
 */
public class ConfigurationServiceImpl extends UnicastRemoteObject implements ConfigurationService, ConfigurationServiceLocal {
	
	// just a logger
	private static Logger LOG = Logger.getLogger(ConfigurationServiceImpl.class);

	// the serializer for the configuration ... the one who stores the data
	@Inject
	@JSONSerializer
	private ConfigSerializer serializer;
	
	/** local listeners for changes */
	private List<ConfigurationChangeListenerLocal> localListeners = new CopyOnWriteArrayList<ConfigurationChangeListenerLocal>();
	
	/** remote listeners for changes */
	private List<ConfigurationChangeListener> remoteListeners = new CopyOnWriteArrayList<ConfigurationChangeListener>();

	// the object holding the configuration
	private Configuration config = new Configuration();

	private boolean initialized;

	public ConfigurationServiceImpl() throws RemoteException {
		super();
	}
	
	@PostConstruct
	public void init() {
		this.config = serializer.readConfiguration();
	}


	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		LOG.debug("setting configuration");
		this.config = config;
		this.serializer.saveConfiguration(config);
		
		// notification of the locally connected listeners (if any)
		for (ConfigurationChangeListenerLocal configurationChangeListenerLocal: localListeners) {
			configurationChangeListenerLocal.onConfigurationChange();	
		}
		
		// notification of the remote listeners (if any)
		for (ConfigurationChangeListener configurationChangeListener : remoteListeners) {
			try {
				configurationChangeListener.onConfigurationChange();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void updateConfig(Configuration config) {
		
		LOG.debug("updating configuration");
		
		//TODO change instead of overwriting
		this.config = config;
		this.serializer.saveConfiguration(config);
		
		for (ConfigurationChangeListenerLocal localListener : localListeners) {
			localListener.onConfigurationChange();	
		}
		
		for (ConfigurationChangeListener remoteListener : remoteListeners) {
			try {
				remoteListener.onConfigurationChange();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void registerForChangesLocally(ConfigurationChangeListenerLocal listener) {
		this.localListeners.add(listener);
	}

	public void registerForChanges(ConfigurationChangeListener listener)
			throws RemoteException {
		LOG.debug("registering: " + listener.getClass().getSimpleName() + ": " + listener);
		this.remoteListeners.add(listener);
	}
}
