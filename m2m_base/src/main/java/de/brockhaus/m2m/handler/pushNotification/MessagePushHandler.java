package de.brockhaus.m2m.handler.pushNotification;

import java.util.List;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * This class is only getting the message and delegates to the worker. 
 * The purpose is internal, not to send messages around. Right now we might have one 
 * worker for WebSockets, maybe later some async. Listener as well.
 * 
 * Example config:
 * 
 	<!-- informing the UI -->
	<bean name="push_notification"
		class="de.brockhaus.m2m.sender.handler.pushNotification.MessagePushHandler" scope="prototype">
		
		<!-- the next handler in line, see below --> 
		<constructor-arg ref="buffer_handler" />
		
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.sender.handler.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.sender.handler.M2MSensorMessage</value>
    	</constructor-arg>

		<!-- The sensor ids which will be pushed -->
		<property name="sensorIds">
			<list>
            	<value>PT_DS1_316233.ED01_AB219_M04.AS.V2251</value>
        	</list>
		</property>
	
		<!-- the worker which does the needful -->
		<property name="pushWorker" ref="websocket_worker" />
	</bean>
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Apr 10, 2015
 *
 */
public class MessagePushHandler extends AbstractM2MMessageHandler {

	// the work will be done in here
	private M2MMessagePushWorker pushWorker;
	
	/** the sensors in which data we are interested */
	private List<String> sensorIds;

	public MessagePushHandler() {
		super();
	}

	public MessagePushHandler(M2MMessageHandler next, String inTypeClassName,
			String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {
		if(sensorIds.contains(message.getSensorId())){
			// delegate to proceed
			pushWorker.doPush((M2MSensorMessage) message);
		}
	}

	public M2MMessagePushWorker getPushWorker() {
		return pushWorker;
	}

	public void setPushWorker(M2MMessagePushWorker pushWorker) {
		this.pushWorker = pushWorker;
	}

	public List<String> getSensorIds() {
		return sensorIds;
	}

	public void setSensorIds(List<String> sensorIds) {
		this.sensorIds = sensorIds;
	}
	
	

}
