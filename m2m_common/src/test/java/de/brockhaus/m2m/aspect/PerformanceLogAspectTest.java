package de.brockhaus.m2m.aspect;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Checking, whether AOP is working. 
 * Can hardly be a JUnit test.
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 28, 2015
 *
 */
public class PerformanceLogAspectTest {

	private static String configFile = "spring-beans.xml";
	
	private static Foo foo;
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(PerformanceLogAspectTest.configFile);
		foo = (Foo) context.getBean("foo");
		
		String result = foo.doFoo("foo");
		System.out.println("Return: " + result);
	}
}
