package de.brockhaus.m2m.sender.opcua;

import org.apache.log4j.Logger;
import org.opcfoundation.ua.common.ServiceResultException;

import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.AddressSpaceException;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.sender.M2MSendingWorker;
import de.brockhaus.m2m.sender.opcua.prosys.OPCUAProsysHandler;

public class OPCUASendingWorker  extends AbstractM2MMessageHandler implements M2MSendingWorker {

	private static final Logger LOG = Logger.getLogger(OPCUASendingWorker.class);
	
	// the handler for dealing with the messages
	private OPCUAProsysHandler handler = new OPCUAProsysHandler(this);
	
	public OPCUASendingWorker() {
		//lazy
	}
	
	public OPCUASendingWorker(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}
	
	public void init() throws ServiceResultException {
		handler.setSender(this);
		// connect to opc and send the new value
		handler.start();	
	}
	
	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		try {
			this.doSend(message);
		} catch (M2MCommunicationException e) {
			LOG.error(e);
		}
	}
	
	@Override
	public void doSend(M2MMessage message) throws M2MCommunicationException {
		try {
			LOG.debug("Sending: " + message.getClass().getSimpleName());
			handler.configureWrite();
		} catch (ServiceResultException | ServiceException | StatusException | AddressSpaceException
				| InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public OPCUAProsysHandler getHandler() {
		return handler;
	}

	public void setHandler(OPCUAProsysHandler handler) {
		this.handler = handler;
	}
}