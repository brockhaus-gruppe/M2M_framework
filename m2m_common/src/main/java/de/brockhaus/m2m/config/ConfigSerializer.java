package de.brockhaus.m2m.config;

/**
 * 
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 7, 2016
 *
 */
public interface ConfigSerializer {

	// TODO throw Exception
	public void saveConfiguration(Configuration config);

	// TODO throw Exception
	public Configuration readConfiguration();
}
