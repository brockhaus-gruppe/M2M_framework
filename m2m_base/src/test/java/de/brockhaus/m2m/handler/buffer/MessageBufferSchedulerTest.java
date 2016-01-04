package de.brockhaus.m2m.handler.buffer;

import java.util.concurrent.TimeUnit;

public class MessageBufferSchedulerTest {
	
	public static void main(String[] args) {
		MessageBufferScheduler scheduler = new MessageBufferScheduler();
		scheduler.setDelay(5000);
		scheduler.setHandler(new M2MMessageTimeBufferHandler());
		scheduler.setInitialDelay(10000);
		scheduler.setTimeUnit(TimeUnit.MILLISECONDS);
		
		scheduler.startSchedule();
	}

}
