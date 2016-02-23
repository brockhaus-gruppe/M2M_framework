package de.brockhaus.m2m.receiver.opcua.prosys;

import java.util.GregorianCalendar;
import java.util.Random;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;

import com.prosysopc.ua.client.MonitoredDataItem;

/**
 * Simulating OPC data which will be send to the (Prosys) Handler.
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 23, 2016
 *
 */
public class OPCProsysSimulationServer {

	private OPCUAProsysHandler handler;
	
	//TODO Dependency Injection
	private long interval = 2000;
	
	public OPCProsysSimulationServer(OPCUAProsysHandler handler) {
		this.handler = handler;
		
	}
	
	public void sendMessages() throws InterruptedException {
		
		while(true) {
			MonitoredDataItem item = new MonitoredDataItem(new NodeId(13, "Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor drilling machine"));
			DataValue oldVal = new DataValue();
			oldVal.setServerTimestamp(new DateTime(new GregorianCalendar()));
			Random r = new Random();
			oldVal.setValue(new Variant(r.nextBoolean()));
			
			DataValue newVal = new DataValue();
			newVal.setServerTimestamp(new DateTime(new GregorianCalendar()));
			newVal.setValue(new Variant(r.nextBoolean()));
			
			handler.onDataChange(item, oldVal, newVal);
			
			Thread.currentThread().sleep(interval);
		}	
	}
}
