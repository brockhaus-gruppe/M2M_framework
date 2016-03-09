package de.brockhaus.m2m.config;

import java.util.Date;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.handler.aggregate.DataAggregationHandler;
import de.brockhaus.m2m.handler.aggregate.DataAggregationHandler.AggregationType;
import de.brockhaus.m2m.message.M2MCommunicationException;
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
 * @author mbohnen, Dec 28, 2015
 *
 */
@Ignore
public class DataAggregationHandlerTest {
	
	private static String configFile = "test/DATAAGGREGATIONONLY_STACKCONFIG.xml";
	
	private static DataAggregationHandler handler;
	
	private M2MMultiMessage multi;

	public static void main(String[] args) {
		DataAggregationHandlerTest.init();
		
		DataAggregationHandlerTest test = new DataAggregationHandlerTest();
		test.testAggregation();

	}
	
	@Test
	public void testAggregation() {
		multi = (M2MMultiMessage) handler.getMessage();
		if(handler.getAggregationType() == AggregationType.AVG) {
			Assert.assertTrue(new Float(multi.getSensorDataMessageList().get(0).getValue()) == 2);
		}
		
		if(handler.getAggregationType() == AggregationType.MIN) {
			Assert.assertTrue(new Float(multi.getSensorDataMessageList().get(0).getValue()) == 1);
		}
		
		if(handler.getAggregationType() == AggregationType.MAX) {
			Assert.assertTrue(new Float(multi.getSensorDataMessageList().get(0).getValue()) == 3);
		}
	}
	
	@BeforeClass
	public static void init() {
		ApplicationContext context= new ClassPathXmlApplicationContext(DataAggregationHandlerTest.configFile);
		handler = (DataAggregationHandler) context.getBean("dataaggregation_handler");
		
		
		M2MMultiMessage multi = new M2MMultiMessage();
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
		
		multi.getSensorDataMessageList().add(m1);
		multi.getSensorDataMessageList().add(m2);
		multi.getSensorDataMessageList().add(m3);
		
		try {
			handler.onMessageEvent(multi);
		} catch (M2MCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
