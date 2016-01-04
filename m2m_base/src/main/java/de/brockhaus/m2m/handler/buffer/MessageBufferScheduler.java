package de.brockhaus.m2m.handler.buffer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @see: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, May 18, 2015
 *
 */
public class MessageBufferScheduler {

	private M2MMessageTimeBufferHandler handler;

	// how long will we wait until first execution
	private long initialDelay;

	// delay between executions
	private long delay;

	// time measurement
	private TimeUnit timeUnit;

	// executor service
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	// the schedule
	private ScheduledFuture<?> schedule = null;
	
	public MessageBufferScheduler() {

	}

	// get the things going
	public void init() {
		this.startSchedule();
	}

	public void startSchedule() {
		schedule = scheduler.scheduleAtFixedRate(handler, this.initialDelay, this.delay, this.timeUnit);
	}

	public void stopSchedule() {
		schedule.cancel(true);
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

	public M2MMessageTimeBufferHandler getHandler() {
		return handler;
	}

	public void setHandler(M2MMessageTimeBufferHandler handler) {
		this.handler = handler;
	}
}
