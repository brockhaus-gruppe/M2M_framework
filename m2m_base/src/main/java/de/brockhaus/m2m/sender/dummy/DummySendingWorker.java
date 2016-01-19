package de.brockhaus.m2m.sender.dummy;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MPlainTextMessage;
import de.brockhaus.m2m.sender.M2MSendingWorker;

/**
 * This sending worker does nothing but printing the message to the console ...
 * 
 * Config example:
 * 
	<!-- sending 2 a dummy -->
	<bean name="dummy-sender"
		class="de.brockhaus.m2m.sender.dummy.DummySendingWorker" scope="prototype">
		
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
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 14, 2016
 *
 */
public class DummySendingWorker extends AbstractM2MMessageHandler implements M2MSendingWorker {

	private static final Logger LOG = Logger.getLogger(DummySendingWorker.class);
	
	public DummySendingWorker() {
		//lazy
	}

	public DummySendingWorker(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	public void doSend(M2MMessage  message) {
		LOG.debug("Sending: \n" + ((M2MPlainTextMessage) message).getSensordata());
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		this.doSend(message);	
	}
}
