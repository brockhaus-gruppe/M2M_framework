package de.brockhaus.m2m.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.receiver.jms.JMSActiveMQReceiverAdapter;

/**
 * Testing how the JMS listener in combination with Apache Cassandra works.
 * Maybe it's a good idea to:
 * a) check ActiveMQ using the web console: http://http://localhost:8161/admin/
 * b) check Cassandra using datastax center 
 * 
 * Project: combase
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 15, 2015
 *
 */
public class JMSReceiver2CassandraTest {

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"JMSRECEIVER2CASSANDRA_STACKCONFIG.xml");
		JMSActiveMQReceiverAdapter adapter = (JMSActiveMQReceiverAdapter) context
				.getBean("jms_adapter");
	}

}
