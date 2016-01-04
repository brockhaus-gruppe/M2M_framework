package de.brockhaus.m2m.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.receiver.file.FileAdapter;

/**
 * Tests how the file adapter works ... data will not be send to somewhere.
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 28, 2015
 *
 */
public class FileAdapter2DummyTest {
	
	public static void main(String[] args) {
		ApplicationContext context= new ClassPathXmlApplicationContext("FILEADAPTER2DUMMY_STACKCONFIG.xml");
		FileAdapter adapter = (FileAdapter) context.getBean("file_adapter");
	}
}
