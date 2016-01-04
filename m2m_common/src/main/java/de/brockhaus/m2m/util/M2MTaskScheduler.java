package de.brockhaus.m2m.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * 
 * 
	<!-- Scheduling -->
	<bean name="m2m_scheduler"
		class="de.brockhaus.m2m.util.M2MTaskScheduler"
		scope="singleton" >
		
		<property name = "adapter" ref = "jdbc_adapter"/>
		
		<!-- how long to wait once started -->
		<property name = "initialDelay">
			<value>0</value>
		</property>
		
		
		<!-- delay between two invocations -->
    	<property name = "delay">
			<value>5000</value>
		</property>
		
		<!-- if granted -->
		<property name="timeUnit">
			<value>MILLISECONDS</value>
		</property>

	</bean>	
 * 
 * @see: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 11, 2015
 *
 */
public class M2MTaskScheduler {
	
	// what will be invoked by the scheduler
	private Runnable target;
	
	// how long will we wait until first execution
	private long initialDelay;
	
	// delay between executions
	private long delay;
	
	// time measurement
	private TimeUnit timeUnit;
	
	// executor service
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	// the schedule
	private ScheduledFuture<?> handle = null;
	
	// get the things going
	public static void main(String[] args) {
		M2MTaskScheduler scheduler = new M2MTaskScheduler();
		scheduler.startSchedule();
	}
	
    public void startSchedule() {
        handle = scheduler.scheduleAtFixedRate(target, this.initialDelay, this.delay, TimeUnit.MILLISECONDS);
    }
    
    public void stopSchedule() {
        handle.cancel(true);
    }
    
    // getter and setter

	public Runnable getTarget() {
		return target;
	}

	public void setTarget(Runnable target) {
		this.target = target;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public ScheduledExecutorService getScheduler() {
		return scheduler;
	}

	public void setScheduler(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}
}
