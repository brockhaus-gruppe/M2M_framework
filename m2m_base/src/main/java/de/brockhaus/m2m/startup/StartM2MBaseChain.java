package de.brockhaus.m2m.startup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.receiver.pojo.M2MMessagePOJOReceiverAdapter;

/**
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 17, 2015
 *
 */
public class StartM2MBaseChain {

	private static String configFile = "M2M_StackConfig.xml";
	
	private static M2MMessagePOJOReceiverAdapter adapter;

	private static List<String> sensors = new ArrayList<String>();
	
	public static void main(String[] args) throws M2MCommunicationException {
		StartM2MBaseChain test = new StartM2MBaseChain();
		test.init();

//		test.sendByNumbers(3, 1000);
		test.sendForever(200);
	}

	private static void init() {

		ApplicationContext context = new ClassPathXmlApplicationContext(StartM2MBaseChain.configFile);
		adapter = (M2MMessagePOJOReceiverAdapter) context.getBean("pojo_adapter");
		
		// populating the list of sensors
		sensors.add("SensorA123");
	}

	private void sendByNumbers(int repeat, int interval)
			throws M2MCommunicationException {

		M2MSensorMessage msg = new M2MSensorMessage();
		msg.setDatatype(M2MDataType.FLOAT);
		M2MMultiMessage multi = new M2MMultiMessage();
		
		for (int i = 0; i < repeat; i++) {
			
			// random element from list
			Random random = new Random(System.currentTimeMillis());
			int index = random.nextInt(this.sensors.size());
	        String randomSensor = sensors.get(index);
			msg.setSensorId(randomSensor);
			
			msg.setTime(new Date(System.currentTimeMillis()));
			
			// setting the value to be randomized within ranges
			float leftLimit = 0F;
			float rightLimit = 1F;
			float genFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);

			msg.setValue(new Float(genFloat).toString());
			msg.setTime(new Date(System.currentTimeMillis()));
			
			multi.getSensorDataMessageList().add(msg);
			
			if(multi.getSensorDataMessageList().size() == 3) {
				//here we go ...
				adapter.onMessageEvent(multi);	
				// housekeeping
				multi.getSensorDataMessageList().clear();
			}
			
			try {
				Thread.currentThread().sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendForever(int interval) throws M2MCommunicationException {

		M2MSensorMessage msg = new M2MSensorMessage();
		msg.setDatatype(M2MDataType.FLOAT);
		
		M2MMultiMessage multi = new M2MMultiMessage();
		
		while (true) {	
			// random element from list
			Random random = new Random(System.currentTimeMillis());
			int index = random.nextInt(this.sensors.size());
	        String randomSensor = sensors.get(index);
			msg.setSensorId(randomSensor);
			
			// setting the value to be randomized within ranges
			float leftLimit = 0F;
			float rightLimit = 1F;
			float genFloat = leftLimit + new Random().nextFloat()
					* (rightLimit - leftLimit);

			msg.setValue(new Float(genFloat).toString());
			msg.setTime(new Date(System.currentTimeMillis()));
			

			multi.getSensorDataMessageList().add(msg);
			
			if(multi.getSensorDataMessageList().size() == 3) {
				//here we go ...
				adapter.onMessageEvent(multi);	
				multi.getSensorDataMessageList().clear();
			}

			try {
				Thread.currentThread().sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
