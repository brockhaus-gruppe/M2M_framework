package de.brockhaus.m2m.sender;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;

/**
 * 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 12, 2016
 *
 */
public class CumulocityAgent extends AbstractM2MMessageHandler implements M2MSendingWorker {
	
	//TODO: read by config file
	private String myUrl;
	private String user;
	private String pwd;
	

	public CumulocityAgent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CumulocityAgent(M2MMessageHandler next, String inTypeClassName, String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
		// TODO Auto-generated constructor stub
	}

	// @see: http://www.cumulocity.com/guides/java/hello-world-basic/
	private void connect() {

	}

	@Override
	public void doSend(M2MMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		// TODO Auto-generated method stub
		
	}

}
