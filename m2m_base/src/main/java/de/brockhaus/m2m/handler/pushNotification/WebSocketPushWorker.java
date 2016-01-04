package de.brockhaus.m2m.handler.pushNotification;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * This worker will do the needful (or delegate to other classes) ...
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 10, 2015
 *
 */
public class WebSocketPushWorker implements M2MMessagePushWorker {
	
	private static final Logger LOG = Logger.getLogger(WebSocketPushWorker.class);
	
	public void doPush(M2MSensorMessage msg) {
		
		LOG.info("pushing: \n" + msg.getSensorId() + "\n" + msg.getValue() + "\n" + msg.getTime() + "\n\n");
		
		//TODO implement
	}

}
