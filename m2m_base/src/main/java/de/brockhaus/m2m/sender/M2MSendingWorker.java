package de.brockhaus.m2m.sender;

import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;


/**
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 11, 2015
 *
 */
public interface M2MSendingWorker extends M2MMessageHandler {
	
	/** we will send nothing but strings */
	public void doSend(M2MMessage message);

}
