package de.brockhaus.m2m.handler.aggregate;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.brockhaus.m2m.handler.aggregate.DataAggregationHandler.AggregationType;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Feb 12, 2016
 *
 */
@Ignore
public class DataAggregationHandlerTest {
	
	private static M2MMultiMessage messages;
	private DataAggregationHandler handler = new DataAggregationHandler();
	
	public static void main(String[] args) {
		DataAggregationHandlerTest.setUp();
		
		DataAggregationHandlerTest test = new DataAggregationHandlerTest();
		test.testHandleMessageAverage();
		test.testHandleMessageMax();
		test.testHandleMessageMin();
	}
	
	@Test
	public void testHandleMessageAverage() {
		handler.setAggregationType(AggregationType.AVG);
		handler.handleMessage(messages);
		M2MMultiMessage result = (M2MMultiMessage) handler.getMessage();
		
		List<M2MSensorMessage> hits = result.getListBySensor("SensorA");
		Assert.assertTrue(hits.size() == 1);
		
		Assert.assertTrue(new Float(hits.get(0).getValue()) == 2);
	}
	
	@Test
	public void testHandleMessageMin() {
		handler.setAggregationType(AggregationType.MIN);
		handler.handleMessage(messages);
		M2MMultiMessage result = (M2MMultiMessage) handler.getMessage();
		
		List<M2MSensorMessage> hits = result.getListBySensor("SensorA");
		Assert.assertTrue(hits.size() == 1);
		
		Assert.assertTrue(new Float(hits.get(0).getValue()) == 1);
	}
	
	@Test
	public void testHandleMessageMax() {
		handler.setAggregationType(AggregationType.MAX);
		handler.handleMessage(messages);
		M2MMultiMessage result = (M2MMultiMessage) handler.getMessage();
		
		List<M2MSensorMessage> hits = result.getListBySensor("SensorA");
		Assert.assertTrue(hits.size() == 1);
		
		Assert.assertTrue(new Float(hits.get(0).getValue()) == 3);
	}
	

	
	@BeforeClass
	public static void setUp() {
		messages = new M2MMultiMessage();
		M2MSensorMessage m1 = new M2MSensorMessage();
		m1.setDatatype(M2MDataType.FLOAT);
		m1.setSensorId("SensorA");
		m1.setTime(new Date(System.currentTimeMillis()));
		m1.setValue("1");
		
		M2MSensorMessage m2 = new M2MSensorMessage();
		m2.setDatatype(M2MDataType.FLOAT);
		m2.setSensorId("SensorA");
		m2.setTime(new Date(System.currentTimeMillis()));
		m2.setValue("2");
		
		
		M2MSensorMessage m3 = new M2MSensorMessage();
		m3.setDatatype(M2MDataType.FLOAT);
		m3.setSensorId("SensorA");
		m3.setTime(new Date(System.currentTimeMillis()));
		m3.setValue("3");
		
		messages.getSensorDataMessageList().add(m1);
		messages.getSensorDataMessageList().add(m2);
		messages.getSensorDataMessageList().add(m3);
	}

}
