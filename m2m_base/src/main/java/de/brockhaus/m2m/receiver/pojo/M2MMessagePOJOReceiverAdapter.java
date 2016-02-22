package de.brockhaus.m2m.receiver.pojo;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessageReceiverLifecycle;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * Just something you can send to in plain java ...
 * 
 * Example config:
 * 
	<bean name="pojo_adapter" class="de.brockhaus.m2m.receiver.pojo.POJOAdapter" scope="prototype">

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.sender.handler.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.sender.handler.M2MSensorMessage</value>
    	</constructor-arg>
	</bean>
 * 
 * Project: communication
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 14, 2015
 *
 */
public class M2MMessagePOJOReceiverAdapter extends AbstractM2MMessageHandler implements M2MMessageReceiverLifecycle {
	
	private static final Logger LOG = Logger.getLogger(M2MMessagePOJOReceiverAdapter.class);
	
	public M2MMessagePOJOReceiverAdapter(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);	
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		
		LOG.debug("handling message");

		// lazy, nothing happens here, 
		// normally you would handle the message, convert it to a new one
		// and set the message but as the outgoing one is the same as the incoming one we don't
		// need to do anything.
		// so this is pro forma ...
		this.setMessage(message);
	}

	@Override
	public void start() {
		// lazy, nothing to do
		try {
			sendByNumbers(6, 1000);
		} catch (M2MCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void stop() {
		// lazy, nothing to do
		
	}
	
	private void sendByNumbers(int repeat, int interval)
			throws M2MCommunicationException {

		M2MSensorMessage msg = new M2MSensorMessage();
		msg.setDatatype(M2MDataType.BOOLEAN);
		for (int i = 0; i < repeat; i++) {
			msg.setSensorId("");
			msg.setValue(new Boolean("true").toString());
			msg.setTime(new Date(System.currentTimeMillis()));
			this.onMessageEvent(msg);	
		
			try {
				Thread.currentThread().sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		}
	}
}