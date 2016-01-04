package de.brockhaus.m2m.receiver.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;

/**
 * The remote interface which will be exposed ...
 * 
 * Project: communication.receiver
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 27, 2015
 *
 */
public interface M2MMessageRMIReceiver extends Remote {
	
	public void dealWithMessage(M2MMessage msg) throws RemoteException, M2MCommunicationException;
}
