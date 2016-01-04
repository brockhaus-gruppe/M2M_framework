package de.brockhaus.m2m.handler.filter;

import java.rmi.RemoteException;

import de.brockhaus.m2m.config.ConfigurationChangeListener;
import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;

/**
 * A filter which supresses values not following a criteria ...
 * 
 *  TODO implement
 *  
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jul 25, 2015
 *
 */
public class M2MMessageSensorValueHandler extends AbstractM2MMessageHandler implements ConfigurationChangeListener{

	@Override
	public void onConfigurationChange() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {
		// TODO Auto-generated method stub
		
	}

}
