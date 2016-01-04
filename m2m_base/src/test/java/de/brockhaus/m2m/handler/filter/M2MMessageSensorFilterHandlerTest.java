package de.brockhaus.m2m.handler.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class M2MMessageSensorFilterHandlerTest {

	public static void main(String[] args) {
		ApplicationContext context= new ClassPathXmlApplicationContext("test/FILTERONLY_STACKCONFIG.xml");
		M2MMessageSensorFilterHandler handler = (M2MMessageSensorFilterHandler) context.getBean("sensorfilter_handler");
		while(true);
	}
}
