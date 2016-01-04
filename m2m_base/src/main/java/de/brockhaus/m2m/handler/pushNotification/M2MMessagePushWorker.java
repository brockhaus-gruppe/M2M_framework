package de.brockhaus.m2m.handler.pushNotification;

import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * Will be replaced by Server-Side-Events ...
 * 
 * Example config:
 * 
 	<!-- informing the UI -->
	<bean name="push_notification"
		class="de.brockhaus.m2m.sender.handler.pushNotification.MessagePushHandler" scope="prototype">
		
		<!-- the next handler in line, see below --> 
		<constructor-arg ref="buffer-handler" />
		
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
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 23, 2015
 *
 */
public interface M2MMessagePushWorker {
	
	public void doPush(M2MSensorMessage msg);
	
}
