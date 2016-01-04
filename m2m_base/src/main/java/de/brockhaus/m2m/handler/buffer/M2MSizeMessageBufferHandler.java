package de.brockhaus.m2m.handler.buffer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * The very first implementation of a buffer ... Lots of room for improvement.
 * We need to test in a multi-threaded environment as well.
 * 
 * Example config:
 * 
 	<!-- buffering -->
	<bean name="buffer_handler"
		class="de.brockhaus.m2m.handler.buffer.M2MSizeMessageBufferHandler" scope="singleton">
		
		<!-- the next handler in line, see below  -->
		<constructor-arg ref="json_converter" />		
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MMultiMessage</value>
    	</constructor-arg>
    	
	
		<!-- will we wait for the size -->
		<property name="threshold" value = "2" />
		
		<!-- if buffer filled we go here -->
		<property name="nextOnBufferFull" ref = "json_converter" />
		<property name="nextOnBufferPart">
			<null />
		</property>
		

	</bean>
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Apr 11, 2015
 *
 */
//TODO rename to M2MSizeMessageBufferHandler
public class M2MSizeMessageBufferHandler extends AbstractM2MMessageHandler {

	private static final Logger LOG = Logger.getLogger(M2MSizeMessageBufferHandler.class);

	// how many messages we want to buffer
	private int threshold;
	// the buffer itself
	private List<M2MSensorMessage> buffer = new ArrayList<M2MSensorMessage>();

		
	// where to go once the buffer is filled
	private M2MMessageHandler nextOnBufferFull;
	
	// where to go once the buffer is not filled up to threshold
	private M2MMessageHandler nextOnBufferPart;
	
	
	public M2MSizeMessageBufferHandler() {
		super();
	}

	public M2MSizeMessageBufferHandler(M2MMessageHandler next, String inTypeClassName,
			String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {

		this.buffer.add((M2MSensorMessage) message);

		if (this.checkBufferIsFull()) {
			LOG.debug("Threshold NOT reached");
			next = this.nextOnBufferPart;
		} else {
			next = this.nextOnBufferFull;
			LOG.debug("Threshold reached, buffer contains: " + buffer.size());
			List<M2MSensorMessage> clone = (List<M2MSensorMessage>) ((ArrayList<M2MSensorMessage>) buffer).clone();

			M2MMultiMessage msg = new M2MMultiMessage();
			msg.setSensorDataMessageList(clone);
			this.setMessage(msg);
			
			super.setCont(true);
			buffer.clear();
		}
	}

	private boolean checkBufferIsFull() {

		if (this.buffer.size() >= this.threshold) {
			return false;
		}

		return true;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public M2MMessageHandler getNextOnBufferFull() {
		return nextOnBufferFull;
	}

	public void setNextOnBufferFull(M2MMessageHandler nextOnBufferFull) {
		this.nextOnBufferFull = nextOnBufferFull;
	}

	public M2MMessageHandler getNextOnBufferPart() {
		return nextOnBufferPart;
	}

	public void setNextOnBufferPart(M2MMessageHandler nextOnBufferPart) {
		this.nextOnBufferPart = nextOnBufferPart;
	}
}
