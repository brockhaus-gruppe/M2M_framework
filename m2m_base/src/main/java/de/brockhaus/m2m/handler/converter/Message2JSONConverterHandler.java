package de.brockhaus.m2m.handler.converter;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MPlainTextMessage;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;

/**
 * Converts a message into JSON. The message can be almost everything, take care for correct configuration
 * within the stack.xml file 
 * 
 * Config example:

	<!-- converting the message to JSON -->
	<bean name="json_converter"
		class="de.brockhaus.m2m.sender.converter.Message2JSONConverterHandler" scope="prototype">
		
		<!-- doing nothing  
		<constructor-arg name = "next">
			<null />
		</constructor-arg>
		-->
		<!-- the next handler in line, see below --> 
		<constructor-arg ref="dummy-sender" />
		
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MMultiMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MPlainTextMessage</value>
    	</constructor-arg>
    	
	</bean>
	
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 10, 2015
 *
 */
public class Message2JSONConverterHandler extends AbstractM2MMessageHandler {
	
	private static final Logger LOG = Logger.getLogger(Message2JSONConverterHandler.class);

	public Message2JSONConverterHandler() {
		super();
	}

	public Message2JSONConverterHandler(M2MMessageHandler next,
			String inTypeClassName, String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		LOG.trace("Serialization of: " + message.getClass().getSimpleName());
		String json = JSONBuilderParserUtil.getInstance().toJSON(message);
		LOG.trace("Serialization 2 JSON: \n" + json + "\n\n");
		
		M2MPlainTextMessage msg = new M2MPlainTextMessage();
		msg.setSensordata(json);
		
		this.setMessage(msg);
	}
}
