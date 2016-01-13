package de.brockhaus.m2m.web.rest;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.config.ConfigurationService;
import de.brockhaus.m2m.util.ConfigServerRMIConnector;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;

/**
 * A restful service providing access to the configuration
 * 
 * 
 * Project: integration_web
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, May 17, 2015
 *
 */
@Path("/config")
public class ConfigHandlerRS {

	private static final Logger LOG = Logger.getLogger(ConfigHandlerRS.class);

	/** the service itself */
	private ConfigurationService configService;

	@PostConstruct
	private void init() throws Exception {
		this.configService = ConfigServerRMIConnector.doConnectRemotely();
	}

	/**
	 * invoke like this:
	 * http://localhost:8080/sensorData/rest/config/getAllElements
	 * 
	 * @return
	 * @throws RemoteException
	 */
	@GET
	@Path("/getAllElements")
	public String getAllElements() throws RemoteException {
		String ret = "No data";
		try {
			Set<String> hit = configService.getConfig().getAllElements();
			if (hit.size() != 0) {
				ret = JSONBuilderParserUtil.getInstance().toJSON(hit);
			}
		} catch (RemoteException e) {
			LOG.error(e);
			throw e;
		}
		return ret;
	}

	/**
	 * invoke like this:
	 * http://localhost:8080/sensorData/rest/config/getConfig4Element/sensors
	 * 
	 * @param element
	 * @return
	 * @throws RemoteException
	 */
	@GET
	@Path("/getConfig4Element/{element_id}")
	public String getConfig4Element(@PathParam("element_id") String element)
			throws RemoteException {
		String ret = "No data";
		try {
			HashMap<String, String> hit = configService.getConfig()
					.getAllEntriesForElement(element);
			if (hit.size() != 0) {
				ret = JSONBuilderParserUtil.getInstance().toJSON(hit);
			}
		} catch (RemoteException e) {
			LOG.error(e);
			throw e;
		}
		return ret;
	}
	
	@POST
	@Path("/setConfig4Element")
	public void setConfig4Element(String configAsJSON)	throws RemoteException {
		LOG.debug("http POST: " + configAsJSON );
		Configuration config = JSONBuilderParserUtil.getInstance().fromJSON(Configuration.class, configAsJSON);
		this.configService.updateConfig(config);
	}

	/**
	 * just to get to know whether the service is listening
	 * 
	 * @return
	 */
	@GET
	@Path("/ping")
	public String ping() {
		return "pong";
	}
}
