package de.brockhaus.m2m.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.receiver.file.FileAdapter;

/**
 * Tests how the file adapter works ... data will not be send to somewhere.
 * 
 * Project: communication
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 14, 2015
 *
 */
public class FileAdapter2CassandraTest {
	
	public static void main(String[] args) {
		ApplicationContext context= new ClassPathXmlApplicationContext("FILEADAPTER2CASSANDRA_STACKCONFIG.xml");
		FileAdapter adapter = (FileAdapter) context.getBean("file_adapter");
	}
}
