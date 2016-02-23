package de.brockhaus.m2m.integration.config;

import org.jboss.weld.environment.se.Weld;

import de.brockhaus.m2m.config.ConfigurationServiceLocal;

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
public class ConfigurationServiceFactory {
	
	public static ConfigurationServiceLocal getConfigurationServiceLocal() {
		
		ConfigurationServiceLocal local = new Weld().initialize().instance().select(ConfigurationServiceImpl.class).get();
		return local;
	}

}
