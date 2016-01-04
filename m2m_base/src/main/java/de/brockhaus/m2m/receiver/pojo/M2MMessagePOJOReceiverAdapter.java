package de.brockhaus.m2m.receiver.pojo;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;

/**
 * Just something you can send to in plain java ...
 * 
 * Example config:
 * 
	<bean name="pojo_adapter" class="de.brockhaus.m2m.receiver.pojo.POJOAdapter" scope="prototype">
		<!-- the next handler in line, see below -->
		<constructor-arg ref="rmi-sender" />
		<!-- 
		<constructor-arg ref="push_notification" />
		 -->
		 
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
public class M2MMessagePOJOReceiverAdapter extends AbstractM2MMessageHandler {
	
	private static final Logger LOG = Logger.getLogger(M2MMessagePOJOReceiverAdapter.class);
	
	public M2MMessagePOJOReceiverAdapter(M2MMessageHandler next, String inTypeClassName,
			String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);	
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {
		
		LOG.debug("handling message");

		// lazy, nothing happens here, 
		// normally you would handle the message, convert it to a new one
		// and set the message but as the outgoing one is the same as the incoming one we don't
		// need to do anything.
		// so this is pro forma ...
		this.setMessage(message);
	}
	
	public void doFoo() {
		LOG.debug("doFoo");
	}
}
