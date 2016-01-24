package de.brockhaus.m2m.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.receiver.rmi.M2MMessageRMIReceiver;

/**
 * This is a specific test case for the POJOReceiver ...
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Jan 20, 2016
 *
 */
public class StartM2M_RMI2DummyTest {

	private M2MMessageRMIReceiver adapter;

	private List<String> sensors = new ArrayList<String>();

	public static void main(String[] args)
			throws M2MCommunicationException, MalformedURLException, RemoteException, NotBoundException {
		StartM2M_RMI2DummyTest test = new StartM2M_RMI2DummyTest();
		test.init();
		test.sendByNumbers(3, 1000);
	}

	private void init() throws MalformedURLException, RemoteException, NotBoundException {
		adapter = (M2MMessageRMIReceiver) Naming.lookup("rmi://localhost:1099/m2m_rmi_receiver");
		sensors.add("SensorABC");
		sensors.add("SensorDEF");
		sensors.add("SensorGHI");
		sensors.add("SensorJKL");
	}

	private void sendByNumbers(int repeat, int interval) throws M2MCommunicationException, RemoteException {

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

			// here we go ...
			adapter.dealWithMessage(msg);

			try {
				Thread.currentThread().sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendForever(int interval) throws M2MCommunicationException, RemoteException {

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
			float genFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);

			msg.setValue(new Float(genFloat).toString());
			msg.setTime(new Date(System.currentTimeMillis()));

			multi.getSensorDataMessageList().add(msg);

			adapter.dealWithMessage(multi);

			try {
				Thread.currentThread().sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
