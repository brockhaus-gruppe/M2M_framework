package de.brockhaus.m2m.config;

/**
 * 
 * Project: integration_config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 16, 2015
 *
 */
public interface ConfigSerializer {

	// TODO throw Exception
	public void saveConfiguration(Configuration config);

	// TODO throw Exception
	public Configuration readConfiguration();
}
