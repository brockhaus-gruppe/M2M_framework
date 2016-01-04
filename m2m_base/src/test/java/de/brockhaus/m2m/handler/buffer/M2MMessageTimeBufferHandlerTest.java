package de.brockhaus.m2m.handler.buffer;

import java.util.Date;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MSensorMessage;

public class M2MMessageTimeBufferHandlerTest {

	private static M2MMessageTimeBufferHandler handler;

	public static void main(String[] args) throws M2MCommunicationException {

		M2MMessageTimeBufferHandlerTest.init();
		
		M2MMessageTimeBufferHandlerTest test = new M2MMessageTimeBufferHandlerTest();
		test.sendByNumbers(2, 1000);
		
	}

	public static void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext("test/MESSAGETIMEBUFFERONLY_STACKCONFIG.xml");
		handler = (M2MMessageTimeBufferHandler) context.getBean("timebuffer_handler");
	}

	private void sendByNumbers(int repeat, int interval) throws M2MCommunicationException {

		for (int i = 0; i < repeat; i++) {

			M2MSensorMessage msg = new M2MSensorMessage();
			msg.setDatatype(M2MDataType.FLOAT);
			msg.setSensorId("SensorA");
			msg.setTime(new Date(System.currentTimeMillis()));

			// setting the value to be randomized within ranges
			float leftLimit = 0F;
			float rightLimit = 1F;
			float genFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);

			msg.setValue(new Float(genFloat).toString());
			msg.setTime(new Date(System.currentTimeMillis()));

			// here we go ...
			handler.onMessageEvent(msg);
			
			try {
				Thread.currentThread().sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
