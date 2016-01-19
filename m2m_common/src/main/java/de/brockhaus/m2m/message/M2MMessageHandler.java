package de.brockhaus.m2m.message;


/**
 * 
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 15, 2016
 *
 */
public interface M2MMessageHandler {
	public <T extends M2MMessage> void onMessageEvent(T message) throws M2MCommunicationException;
	public abstract <T extends M2MMessage> void handleMessage(T message);
}
