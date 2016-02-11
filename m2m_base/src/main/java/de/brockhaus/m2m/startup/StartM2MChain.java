package de.brockhaus.m2m.startup;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessageReceiverLifecycle;

/**
 * The generic starter
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 25, 2016
 *
 */
public class StartM2MChain {

	private static String configFile = "M2M_OPCUA2C8Y.xml";

	private static M2MMessageReceiverLifecycle adapter;

	public static void main(String[] args) {
		if (Arrays.asList(args).size() > 0) {
			configFile = args[0];
		}
		StartM2MChain.init();
	}
	
	public StartM2MChain() {
		StartM2MChain.init();
	}
	
	public StartM2MChain(String chainConfig) {
		this.configFile = chainConfig;
		StartM2MChain.init();
	}

	private static void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext(StartM2MChain.configFile);
		adapter = (M2MMessageReceiverLifecycle) context.getBean("start");
		adapter.start();
	}

	public static M2MMessageHandler getAdapter() {
		return (M2MMessageHandler) adapter;
	}
}
