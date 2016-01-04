package de.brockhaus.m2m.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.receiver.file.FileAdapter;

/**
 * Tests whether the file adapter works fine and the data will be send to messaging infrastructure.
 * 
 * Maybe it's a good idea to start ActiveMQ and a receiver beforehand ...
 * 
 * Project: communication
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 14, 2015
 *
 */
public class FileAdapter2JMSTest {
	
	public static void main(String[] args) {
		ApplicationContext context= new ClassPathXmlApplicationContext("FILEADAPTER2JMS_STACKCONFIG.xml");
		FileAdapter adapter = (FileAdapter) context.getBean("file_adapter");
	}
}
