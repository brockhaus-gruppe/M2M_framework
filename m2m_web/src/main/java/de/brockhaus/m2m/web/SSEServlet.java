package de.brockhaus.m2m.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.web.util.DataContainer;

/**
 * The incoming data will be pushed to the connected html page 
 * (a primitive example is provided by SSE.html).
 * 
 * For more details on Server-Sent-Events:
 * http://milestonenext.blogspot.de/2013/07/html5-server-sent-events-sample-with.html
 * https://blog.rasc.ch/?p=2043
 *
 * Project: m2m-web
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 13, 2016
 *
 */
@WebServlet("/sseDataPush")
public class SSEServlet extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(SSEServlet.class);
	
	@Inject
	private DataContainer container;
	
	private PrintWriter writer;
	
	//TODO external configuration needed
	// 0 means immediately
	private String retry = "0";
	
	private String eventType = "message";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Connection", "keep-alive");

		writer = response.getWriter();
		writer.write("id: " + "SensorData" + "\n");
		writer.write("event: " + eventType + "\n");
		writer.write("retry: " + retry + "\n");
		
		LOG.trace("checking for sending");
		if (this.container.getSize() > 0) {

			LOG.debug("Sending towards client");
			for (String json : container.getValues()) {
				String msg = json.replaceAll("\n", "");
				writer.write("data: " + msg + "\n\n");
				
				// Do not close the writer!
				writer.flush();
				container.removeValue(json);
			}	

		} else {
			// Do not close the writer!
			writer.flush();	
		}	
	}
}
