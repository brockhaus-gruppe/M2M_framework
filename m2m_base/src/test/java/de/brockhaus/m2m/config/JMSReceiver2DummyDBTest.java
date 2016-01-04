package de.brockhaus.m2m.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.receiver.jms.JMSActiveMQReceiverAdapter;

/**
 * You might want to check within the ActiveMQ console:
 * http://http://localhost:8161/admin/
 * 
 * Project: communication
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 14, 2015
 *
 */
public class JMSReceiver2DummyDBTest {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"JMSRECEIVER2DUMMYDB_STACKCONFIG.xml");
		JMSActiveMQReceiverAdapter adapter = (JMSActiveMQReceiverAdapter) context.getBean("jms_adapter");
	}

}
