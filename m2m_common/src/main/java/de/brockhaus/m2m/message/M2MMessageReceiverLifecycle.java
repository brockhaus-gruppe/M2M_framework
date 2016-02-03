package de.brockhaus.m2m.message;

/**
 * Handling the receiver / chain life cycle
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Feb 3, 2016
 *
 */
public interface M2MMessageReceiverLifecycle {
	
	public void start();
	public void stop();

}
