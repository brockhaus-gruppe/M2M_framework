package de.brockhaus.m2m.config;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.receiver.pojo.M2MMessagePOJOReceiverAdapter;
import de.brockhaus.m2m.util.ConfigServerRMIConnector;

/**
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 17, 2015
 *
 */
public class POJOAdapter2RMITest {

	private M2MMessagePOJOReceiverAdapter adapter;
	private static long currentMillies = System.currentTimeMillis();
	
	private List<String> sensors = new ArrayList<String>();

	public static void main(String[] args) throws M2MCommunicationException {
		POJOAdapter2RMITest test = new POJOAdapter2RMITest();
		test.init();

		test.sendByNumbers(3, 10);
		// test.sendForever(1000);
	}

	private void init() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"POJOADAPTER2RMI_STACKCONFIG.xml");
		adapter = (M2MMessagePOJOReceiverAdapter) context
				.getBean("pojo_adapter");
		
		//populate the list
		try {
			ConfigurationService config = ConfigServerRMIConnector.doConnect();
			sensors.addAll(config.getConfig().getAllEntriesForElement("sensors").keySet());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void sendByNumbers(int repeat, int interval)
			throws M2MCommunicationException {

		M2MSensorMessage msg = new M2MSensorMessage();
		msg.setDatatype(M2MDataType.FLOAT);

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
			
			// send through adapter
			adapter.onMessageEvent(msg);

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
			
			//send trough adapter
			adapter.onMessageEvent(msg);

			try {
				Thread.currentThread().sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
