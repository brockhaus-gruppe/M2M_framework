package de.brockhaus.m2m.config.aspect;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * wrapping around the Foo class 4 aop testing
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 27, 2015
 *
 */
public class PerformanceLogAspectTest {

	private static String configFile = "aop/aoptest-config.xml";
	
	private static Foo foo;
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(PerformanceLogAspectTest.configFile);
		foo = (Foo) context.getBean("foo");
		
		String result = foo.doFoo("bar");
		System.out.println("Return: " + result);
	}
}
