package de.brockhaus.m2m.message;


/**
 * A so to say String representation of a/many sensor message/s ...
 * 
 * Project: comcommon
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 12, 2015
 *
 */
public class M2MPlainTextMessage extends M2MMessage {
	
	private String sensordata;

	public String getSensordata() {
		return sensordata;
	}

	public void setSensordata(String sensordata) {
		this.sensordata = sensordata;
	}
	
}
