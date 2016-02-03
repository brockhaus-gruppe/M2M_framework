package de.brockhaus.m2m.receiver.opcua;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.receiver.opcua.prosys.OPCUAProsysHandler;

/**
 * 
	<!-- OPCUA based receiving -->
	<bean name="start"
		class="de.brockhaus.m2m.receiver.opcua.M2MMessageOpcUaReceiver" 
		parent = "abstract_handler"
		scope="singleton" >
		
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	<constructor-arg ref="prosys_handler" />
    	<constructor-arg>
        	<value type="java.lang.Boolean">true</value>
    	</constructor-arg>
    	
    	<property name="handler" ref="prosys_handler" />
	</bean>
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Jan 23, 2016
 *
 */
public class M2MMessageOpcUaReceiver extends AbstractM2MMessageHandler {

	// just a logger
	private static final Logger LOG = Logger.getLogger(M2MMessageOpcUaReceiver.class);

	// just simulated or connected?
	private boolean simMode;
	
	// the concrete handler
	private OPCUAHandler handler;
	
	// what type of implementation might be used (to be extended)
	public enum OPCHandlerProvider {PROSYS};
	
	// what kind of implementation is currently used
	public OPCHandlerProvider currentProvider = OPCHandlerProvider.PROSYS;
	
	
	public M2MMessageOpcUaReceiver(String inTypeClassName, String outTypeClassName, OPCUAHandler handler, boolean simMode) {
		super(inTypeClassName, outTypeClassName);
		
		switch(this.currentProvider) {
			case PROSYS: handler = (OPCUAProsysHandler) handler; break;
		}
		
		this.simMode = simMode;
		handler.setReceiver(this);
		
		LOG.debug("Simulation Mode: " + this.simMode);
		if (simMode != true) {
			handler.start();
			// keep the things rolling (and wait for messages)
			while (true);
		} else {
			handler.start();
		}
	}
	
	public M2MMessageOpcUaReceiver() {
		//lazy
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message: " + message.toString());
	}

	public boolean isSimMode() {
		return simMode;
	}

	public void setSimMode(boolean simMode) {
		this.simMode = simMode;
	}

	public OPCUAHandler getHandler() {
		return handler;
	}

	public void setHandler(OPCUAHandler handler) {
		this.handler = handler;
	}

	public OPCHandlerProvider getCurrentProvider() {
		return currentProvider;
	}

	public void setCurrentProvider(OPCHandlerProvider currentProvider) {
		this.currentProvider = currentProvider;
	}
}
