package de.brockhaus.m2m.sender;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MPlainTextMessage;

/**
 * This sending worker does nothing but printing the message to the console ...
 * 
 * Config example:
 * 
	<!-- sending 2 a dummy -->
	<bean name="dummy-sender"
		class="de.brockhaus.m2m.sender.DummySendingWorker" scope="prototype">
		
		<!-- the last in line, no following handler -->
		<constructor-arg name = "next">
			<null />
		</constructor-arg>

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MPlainTextMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MSensorMessage</value>
    	</constructor-arg>
	
	</bean>
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 11, 2015
 *
 */
public class DummySendingWorker extends AbstractM2MMessageHandler implements M2MSendingWorker {

	private static final Logger LOG = Logger.getLogger(DummySendingWorker.class);
	
	

	public DummySendingWorker() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DummySendingWorker(M2MMessageHandler next, String inTypeClassName,
			String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
		// TODO Auto-generated constructor stub
	}

	public void doSend(M2MMessage  message) {
		LOG.debug("Sending: \n" + ((M2MPlainTextMessage) message).getSensordata());
		
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {

		this.doSend(message);
		
	}
}
