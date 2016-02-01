package de.brockhaus.m2m.receiver.opcua;

/**
 * The common interface for all OPC/UA related handlers
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 27, 2016
 *
 */
public interface OPCUAHandler {
	void setReceiver(M2MMessageOpcUaReceiver receiver);
	void startSimulation();
}
