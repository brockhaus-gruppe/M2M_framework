package de.brockhaus.m2m.integration.config.c8y;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * this mapping contains all data relevant for device mapped to cumulocity (e.g. GIds) 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Feb 22, 2016
 *
 */

@XmlRootElement
public class C8YDeviceMapping implements Serializable {
	
	private int arrayIndex;
	private String deviceName;
	private String ownGId;
	private String parentGId;
	
	public C8YDeviceMapping() {

	}
	
	public C8YDeviceMapping(int arrayIndex, String deviceName, String ownGId, String parentGId) {
		super();
		this.arrayIndex = arrayIndex;
		this.deviceName = deviceName;
		this.ownGId = ownGId;
		this.parentGId = parentGId;
	}

	public int getArrayIndex() {
		return arrayIndex;
	}

	public void setArrayIndex(int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getOwnGId() {
		return ownGId;
	}

	public void setOwnGId(String ownGId) {
		this.ownGId = ownGId;
	}

	public String getParentGId() {
		return parentGId;
	}

	public void setParentGId(String parentGId) {
		this.parentGId = parentGId;
	}

	@Override
	public String toString() {
		return this.arrayIndex + ";" + this.ownGId + ";" + this.parentGId + ";" + this.deviceName;
	}
}
