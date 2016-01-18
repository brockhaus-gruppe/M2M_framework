package de.brockhaus.m2m.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.receiver.file.FileAdapter;

public class FileAdapter2RddTest {
	public static void main(String[] args) {
		ApplicationContext context= new ClassPathXmlApplicationContext("FILEADAPTER2RDD_STACKCONFIG.xml");
		FileAdapter adapter = (FileAdapter) context.getBean("file_adapter");
	}

}
