package de.brockhaus.m2m.message;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * Project: comcommon
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 21, 2015
 *
 */
public class M2MMultiMessage extends M2MMessage {

	private List<M2MSensorMessage> sensorDataMessageList = new ArrayList<M2MSensorMessage>();

	public List<M2MSensorMessage> getSensorDataMessageList() {
		return sensorDataMessageList;
	}

	public void setSensorDataMessageList(List<M2MSensorMessage> sensorDataList) {
		this.sensorDataMessageList = sensorDataList;
	}
	
	public List<M2MSensorMessage> getListBySensor(String sensorId) {
		
		List<M2MSensorMessage> list = new ArrayList<M2MSensorMessage>();
		for (M2MSensorMessage m2mSensorMessage : sensorDataMessageList) {
			if(m2mSensorMessage.getSensorId().equals(sensorId)) {
				list.add(m2mSensorMessage);
			}
		}
		
		return list;
	}
}
