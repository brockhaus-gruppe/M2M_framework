package de.brockhaus.m2m.integration.rmiserver;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.apache.log4j.Logger;
import org.jboss.weld.environment.se.Weld;

import de.brockhaus.m2m.integration.config.ConfigurationServiceImpl;

/**
 * Starting the configuration server
 * 
 * Project: integration_config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 17, 2015
 *
 */
public class StartServer {

	private static final Logger LOG = Logger.getLogger(StartServer.class);
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException, UnknownHostException {
		
		String localhostname = java.net.InetAddress.getLocalHost().getHostName();
		
		int port = 1399;
		
		// using WELD container
		ConfigurationServiceImpl service = new Weld().initialize().instance().select(ConfigurationServiceImpl.class).get();
		LocateRegistry.createRegistry(1399);
		
		Naming.bind("rmi://"+localhostname+":"+port+"/config_service", service);
		
		LOG.info("Config Service started: " + localhostname + ":" + port);
	}

}
