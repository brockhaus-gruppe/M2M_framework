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

	private String configFile = "M2M_OPCUA2C8Y.xml";

	private M2MMessageReceiverLifecycle adapter;

	public static void main(String[] args) {
		if (Arrays.asList(args).size() > 0) {
			StartM2MChain chain = new StartM2MChain(args[0]);
			chain.init();
		} else {
			new StartM2MChain();
		}
	}
	
	public StartM2MChain() {
		this.init();
	}
	
	public StartM2MChain(String chainConfig) {
		this.configFile = chainConfig;
		this.init();
	}

	private void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext(this.configFile);
		adapter = (M2MMessageReceiverLifecycle) context.getBean("start");
		adapter.start();
	}

	// the first adapter within the chain
	public M2MMessageHandler getAdapter() {
		return (M2MMessageHandler) adapter;
	}
}