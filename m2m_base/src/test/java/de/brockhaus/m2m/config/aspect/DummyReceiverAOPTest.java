package de.brockhaus.m2m.config.aspect;

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
 * Testing the DummyReceiver to be wrapped by aspect
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 27, 2015
 *
 */
public class DummyReceiverAOPTest {

	private static String configFile = "aop/AOP_POJOADAPTER_STACKCONFIG.xml";

	private static M2MMessagePOJOReceiverAdapter adapter;

	private static List<String> sensors = new ArrayList<String>();

	public static void main(String[] args) throws M2MCommunicationException {
		DummyReceiverAOPTest.init();
		
		DummyReceiverAOPTest test = new DummyReceiverAOPTest();
		test.sendByNumbers(3, 100);
	}

	private static void init() {

		ApplicationContext context = new ClassPathXmlApplicationContext(DummyReceiverAOPTest.configFile);
		adapter = (M2MMessagePOJOReceiverAdapter) context.getBean("pojo_adapter");

		// populating the list of sensors
		sensors.add("SensorA123");
	}

	private void sendByNumbers(int repeat, int interval) throws M2MCommunicationException {

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

			if (multi.getSensorDataMessageList().size() == 3) {
				// here we go ...
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
}
