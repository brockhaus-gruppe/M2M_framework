package de.brockhaus.m2m.message;

/**
 * 
 * Project: comcommon
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 10, 2015
 *
 */
public class M2MCommunicationException extends Exception {
	
	public M2MCommunicationException() {
		super();
	}

	public M2MCommunicationException(String msg) {
		super(msg);
	}

	public M2MCommunicationException(Throwable cause) {
		super(cause);
	}
}
