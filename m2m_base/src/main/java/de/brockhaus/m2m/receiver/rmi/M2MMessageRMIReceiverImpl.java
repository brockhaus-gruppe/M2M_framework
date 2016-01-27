package de.brockhaus.m2m.receiver.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;

/**
 * The Implementation with respect to UnicastRemoteObject and the interface ...
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 24, 2016
 *
 */
public class M2MMessageRMIReceiverImpl extends UnicastRemoteObject implements M2MMessageRMIReceiver {
	
	private static final Logger LOG = Logger.getLogger(M2MMessageRMIReceiverImpl.class);
	
	// delegating for 'normal' continuation
	private M2MMessageRMIReceiverAdapter adapterCallBack;

	protected M2MMessageRMIReceiverImpl(M2MMessageRMIReceiverAdapter adapterCallBack) throws RemoteException {
		super();
		this.adapterCallBack = adapterCallBack;
	}

	@Override
	public void dealWithMessage(M2MMessage msg) throws RemoteException, M2MCommunicationException {
		LOG.info("receiving message through RMI");
		adapterCallBack.onMessageEvent(msg);
	}
}
