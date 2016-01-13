package de.brockhaus.m2m.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections15.buffer.CircularFifoBuffer;

import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;

/**
 * This is a buffer for the last n values of a certain sensor ...
 * 
 * As Jersey will NOT work together with CDI, we can't inject it ...
 * TODO maybe we should move this to common
 * 
 * https://commons.apache.org/proper/commons-collections/javadocs/api-3.2.1/org/apache/commons/collections/buffer/CircularFifoBuffer.html
 * http://sourceforge.net/projects/collections/
 *
 * Project: m2m-web
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 13, 2016
 *
 */
// @Singleton
public class CircularBufferDataContainer {
	
	private static final CircularBufferDataContainer THIS = new CircularBufferDataContainer();
	
	//TODO external configuration needed
	private int capacity = 10;
	
	private HashMap<String, CircularFifoBuffer<M2MSensorMessage>> values = new HashMap<String, CircularFifoBuffer<M2MSensorMessage>>(); 
	
	private CircularBufferDataContainer() {
		// lazy
	}
	
	public static CircularBufferDataContainer getInstance() {
		return THIS;
	}
	
	public void addValueForSensor(String sensorId, M2MSensorMessage msg) {
		if(! this.values.containsKey(sensorId)){
			CircularFifoBuffer<M2MSensorMessage> buffer = new CircularFifoBuffer<M2MSensorMessage>(capacity);
			buffer.add(msg);
			this.values.put(sensorId, buffer);
		} else {
			this.values.get(sensorId).add(msg);
		}
	}
	
	public void addValueForSensor(String json) {
		M2MSensorMessage msg = JSONBuilderParserUtil.getInstance().fromJSON(M2MSensorMessage.class, json);
		this.addValueForSensor(msg.getSensorId(), msg);
	}
	
	public String getValuesForSensorAsJSON(String sensorId) {
		String ret = "No data";
		CircularFifoBuffer<M2MSensorMessage> hit = this.values.get(sensorId);
		
		if(null != hit) {
			List<M2MSensorMessage> list = new ArrayList<M2MSensorMessage>(hit);
			ret = JSONBuilderParserUtil.getInstance().toJSON(list);
		}
		
		return ret;
	}
	
	public List<String> getAllSensorIdsFromBuffer() {
		List<String> ret = new ArrayList<String>();
		ret.addAll(this.values.keySet());
		return ret;
	}
}
