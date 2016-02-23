package de.brockhaus.m2m.integration.config.c8y;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * this mapping contains all data relevant for sensor mapped to cumulocity (e.g. GIds) 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Feb 22, 2016
 *
 */

@XmlRootElement
public class C8YSensorMapping implements Serializable {
	
	private int arrayIndex;
	private String sensorName;
	private String ownGId;
	private String parentGId;
	
	public C8YSensorMapping() {

	}
	
	public C8YSensorMapping(int arrayIndex, String sensorName, String ownGId, String parentGId) {
		super();
		this.arrayIndex = arrayIndex;
		this.sensorName = sensorName;
		this.ownGId = ownGId;
		this.parentGId = parentGId;
	}

	public int getArrayIndex() {
		return arrayIndex;
	}

	public void setArrayIndex(int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
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
		return this.arrayIndex + ";" + this.ownGId + ";" + this.parentGId + ";" + this.sensorName;
	}
}
