package de.brockhaus.m2m.receiver.opcua;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;

public class M2MMessageOpcUaReceiver extends AbstractM2MMessageHandler {
	
	private static final Logger LOG = Logger.getLogger(M2MMessageOpcUaReceiver.class);

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		
	}

}
