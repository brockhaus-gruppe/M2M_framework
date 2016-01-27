package de.brockhaus.m2m.receiver.opcua;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.receiver.opcua.prosys.OPCUAProsysHandler;

/**
 * 
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
		
		handler.setReceiver(this);
		this.simMode = simMode;
		
		LOG.debug("Simulation Mode: " + this.simMode);
		if (simMode != true) {
			// keep the things rolling (and wait for messages)
			while (true);
		} else {
			handler.startSimulation();
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
