package de.brockhaus.m2m.web.rest;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;

/**
 * 
 *
 * Project: m2m-web
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Mar 3, 2016
 *
 */
@Path("/command")
public class CommandHandlerRS {
	
	private static final Logger LOG = Logger.getLogger(CommandHandlerRS.class);
	
	private String chainConfig = "M2M_POJO2OPCUA.xml";
	
	/**
	 * here it goes
{
  "sensorId" : "Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor drilling machine",
  "time" : "2016-03-15,10:25:57",
  "datatype" : "BOOLEAN",
  "value" : "true"
}
	 * @param sensorMessage
	 * @throws M2MCommunicationException
	 */
	
	@POST
	@Path("/sensorMessage")
	public void handleCommand(String sensorMessage) throws M2MCommunicationException {
		LOG.info("message received: " + sensorMessage);
		
		M2MSensorMessage message = JSONBuilderParserUtil.getInstance().fromJSON(M2MSensorMessage.class, sensorMessage);
		
		ApplicationContext context = new ClassPathXmlApplicationContext(this.chainConfig);
		M2MMessageHandler adapter = (M2MMessageHandler) context.getBean("start");
		adapter.onMessageEvent(message);
	}
	
	// can we reach the service?
	@GET
	@Path("/ping")
	public String doPing() {
		LOG.debug("ping");
		return "PONG";
	}

}
