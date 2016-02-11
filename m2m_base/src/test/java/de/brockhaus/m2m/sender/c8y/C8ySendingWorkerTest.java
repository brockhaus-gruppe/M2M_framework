package de.brockhaus.m2m.sender.c8y;

import java.util.Date;

import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.sender.c8y.C8ySendingWorker;

public class C8ySendingWorkerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		M2MSensorMessage msg = new M2MSensorMessage();
		msg.setSensorId("Inputs.Push-button slider 2 rear");
		msg.setTime(new Date(System.currentTimeMillis()));
		msg.setDatatype(M2MDataType.BOOLEAN);
		msg.setValue("TRUE");
		
		C8ySendingWorker csw = new C8ySendingWorker();
		csw.init();
		csw.handleMessage(msg);
	}
}