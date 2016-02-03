package de.brockhaus.m2m.startup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

public class StartM2M_POJO2RDDTest {
	private static StartM2MChain chain;

	private M2MMessageHandler adapter;
	
	private static List<String> sensors = new ArrayList<String>();
	
	public static void main(String[] args) throws M2MCommunicationException {
		chain = new StartM2MChain("M2M_POJO2RDD.xml");
		StartM2M_POJO2RDDTest test = new StartM2M_POJO2RDDTest();
		test.init();
		test.sendByNumbers(3, 1000);
	}
	
	private void init() {
		// populating the list of sensors
		sensors.add("SensorA123");
		adapter = chain.getAdapter();	
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
