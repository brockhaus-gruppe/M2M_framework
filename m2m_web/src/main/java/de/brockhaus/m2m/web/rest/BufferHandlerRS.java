package de.brockhaus.m2m.web.rest;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.config.ConfigurationService;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;
import de.brockhaus.m2m.web.util.CircularBufferDataContainer;

/**
 * 
 * 
 * The are problems in combining CDI with Jersey:
 * http://stackoverflow.com/questions/18963627/how-to-integrate-jax-rs-with-cdi-in-a-servlet-3-0-container
 * http://stackoverflow.com/questions/25436467/cdi-and-hk2-not-working-together
 * 
 * So CDI is not used but:
 * TODO use CDI
 * 
 * Project: communication.web
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 5, 2015
 *
 */
@Path("/buffer")
public class BufferHandlerRS {
	
	private static final Logger LOG = Logger.getLogger(BufferHandlerRS.class);
	
	// @Inject // will NOT work with Jersey
	private CircularBufferDataContainer buffer = CircularBufferDataContainer.getInstance();

	/**
	 * invoke like this: http://localhost:8080/sensorData/rest/buffer/PT_DS1_316233.ED01_AB219_M04.AS.V2251
	 * @param id
	 * @return
	 */
	@GET
	@Path("{sensor_id}")
	public String getBufferForSensor(@PathParam("sensor_id") String id) {
		return buffer.getValuesForSensorAsJSON(id);
	}
	
	@GET
	@Path("/getAllSensorIdsFromBuffer")
	public String getAllSensorIdsFromBuffer() {
		return JSONBuilderParserUtil.getInstance().toJSON(buffer.getAllSensorIdsFromBuffer());
	}
}
