package de.brockhaus.m2m.sender;

import de.brockhaus.m2m.message.M2MMessage;


/**
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 11, 2015
 *
 */
public interface M2MSendingWorker {
	
	/** we will send nothing but strings */
	public void doSend(M2MMessage message);

}
