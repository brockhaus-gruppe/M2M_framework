package de.brockhaus.m2m.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.receiver.rmi.M2MMessageRMIReceiverAdapter;

public class RMIReceiver2DummyDBTest {

	public static void main(String[] args) {
		ApplicationContext context= new ClassPathXmlApplicationContext("RMIADAPTER2DUMMYDB_STACKCONFIG.xml");
		M2MMessageRMIReceiverAdapter adapter = (M2MMessageRMIReceiverAdapter) context.getBean("rmi_adapter");
	}
}
