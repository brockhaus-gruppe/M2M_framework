package de.brockhaus.m2m.message;


/**
 * 
 * Project: comcommon
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 16, 2015
 *
 */
public interface M2MMessageHandler {

	/**
	 * the generic is not really needed here ... just for the show
	 * @param message
	 * @throws M2MCommunicationException
	 */
	public abstract  <T extends M2MMessage> void onMessageEvent(T message)
	        throws M2MCommunicationException;

}
