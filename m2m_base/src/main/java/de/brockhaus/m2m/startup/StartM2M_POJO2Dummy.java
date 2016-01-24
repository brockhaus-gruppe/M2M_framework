package de.brockhaus.m2m.startup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.receiver.pojo.M2MMessagePOJOReceiverAdapter;

/**
 * This class needs to be adapted to the needs of the current receiver ...
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 17, 2015
 *
 */
public class StartM2M_POJO2Dummy {

	// the default config file
	private static String configFile = "M2M_POJO2Dummy.xml";
	
	private static M2MMessagePOJOReceiverAdapter adapter;

	public StartM2M_POJO2Dummy() {
		this.init();
	}

	private void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext(StartM2M_POJO2Dummy.configFile);
		adapter = (M2MMessagePOJOReceiverAdapter) context.getBean("pojo_adapter");
	}

	public static M2MMessageHandler getAdapter() {
		return adapter;
	}
}
