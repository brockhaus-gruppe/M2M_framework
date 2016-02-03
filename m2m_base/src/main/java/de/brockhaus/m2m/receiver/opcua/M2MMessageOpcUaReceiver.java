package de.brockhaus.m2m.receiver.opcua;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageReceiverLifecycle;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.receiver.opcua.prosys.OPCUAProsysHandler;

/**
 * 
	<!-- OPCUA based receiving -->
	<bean name="start"
		class="de.brockhaus.m2m.receiver.opcua.M2MMessageOpcUaReceiver" 
		parent = "abstract_handler"
		init-method="init"
		scope="singleton" >
		
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the handler chain -->
		<constructor-arg ref = "handler_chain" />
        <!-- the OPC handler -->	
    	<constructor-arg ref="prosys_handler" />
    	<!-- are we in simulation mode ? -->
    	<constructor-arg>
        	<value type="java.lang.Boolean">true</value>
    	</constructor-arg>
    	
    	<!-- the handler, which will be used -->
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
public class M2MMessageOpcUaReceiver extends AbstractM2MMessageHandler implements M2MMessageReceiverLifecycle {

	// just a logger
	private static final Logger LOG = Logger.getLogger(M2MMessageOpcUaReceiver.class);

	// just simulated or connected?
	private boolean simMode;

	// the concrete handler
	private OPCUAHandler handler;

	// what type of implementation might be used (to be extended)
	public enum OPCHandlerProvider {
		PROSYS
	};

	// what kind of implementation is currently used
	public OPCHandlerProvider currentProvider = OPCHandlerProvider.PROSYS;

	public M2MMessageOpcUaReceiver(String inTypeClassName, String outTypeClassName, OPCUAHandler handler, boolean simMode) {

		super(inTypeClassName, outTypeClassName);
		this.simMode = simMode;
	}

	public M2MMessageOpcUaReceiver() {
		// lazy
	}

	public void start() {
		
		switch (this.currentProvider) {
		case PROSYS:
			handler = (OPCUAProsysHandler) handler;
			break;
		}

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

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message: " + message.toString());
		M2MMultiMessage m = new M2MMultiMessage();
		m.getSensorDataMessageList().add((M2MSensorMessage) message);
		super.setMessage(m);
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

	@Override
	public void stop() {
		System.exit(0);
		
	}
}
