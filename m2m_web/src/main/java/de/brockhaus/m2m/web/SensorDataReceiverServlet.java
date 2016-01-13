package de.brockhaus.m2m.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.web.util.CircularBufferDataContainer;
import de.brockhaus.m2m.web.util.DataContainer;

/**
 * Dealing with incoming sensor data (M2MMessages):
 * a) buffering it
 * b) pushing it towards SSE
 * 
 * Regarding CDI and Tomcat:
 * http://balusc.blogspot.de/2013/10/how-to-install-cdi-in-tomcat.html
 *
 * Project: m2m-web
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 13, 2016
 *
 */
@WebServlet(name = "sensorDataReceiver", urlPatterns = { "/sensorDataReceiver" })
public class SensorDataReceiverServlet extends HttpServlet {

	private static final Logger LOG = Logger
			.getLogger(SensorDataReceiverServlet.class);

	@Inject
	private DataContainer container;
	
	//TODO put to ConfigService or web.xml
	private boolean useContainer = true;

	// @Inject // will NOT work together with JERSEY
	private CircularBufferDataContainer buffer = CircularBufferDataContainer.getInstance();
	
	//TODO put to ConfigService or web.xml
	private boolean useBuffer = true;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String jsonData = req.getParameter("sensor_data");
		LOG.debug("Received: " + jsonData);

		if (useContainer) {
			// put it to a container so it will be shared, e.g. for server side events
			container.addValue(jsonData);
		}

		if (useBuffer) {
			// put it to a buffer so we can access the buffer 
			// by means of 'get me the last n values'
			buffer.addValueForSensor(jsonData);
		}
	}
	
	public void init() throws ServletException {
		
	}
}
