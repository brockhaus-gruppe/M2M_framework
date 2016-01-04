package de.brockhaus.m2m.receiver.rdbms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.util.M2MTaskScheduler;

/**
 * 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 13, 2015
 *
 */
public class M2MJdbcSchedulerTest {
	
	private static M2MTaskScheduler scheduler;

	public static void main(String[] args) {
		M2MJdbcSchedulerTest.init();
		scheduler.startSchedule();		
	}
	
	public static void init() {
		ApplicationContext context= new ClassPathXmlApplicationContext("test/JDBCSCHEDULER2DUMMY_STACKCONFIG.xml");
		scheduler = (M2MTaskScheduler) context.getBean("m2m_scheduler");		
	}
}
