package de.brockhaus.m2m.handler;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;



/**
 * The abstract super class according to Chain of Responsibility pattern plus Template Method Pattern.
 * 
 * Check out for the methods you have to overwrite and for the methods you might overwrite plus for the
 * methods you can't overwrite. Please note: 
 * 
 * -> everything which should happen you might implement within handleMessage.
 * -> everything starts with 'onMessageEvent' at the first element of the chain
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 1, 2015
 *
 */
public abstract class AbstractM2MMessageHandler implements M2MMessageHandler {
	
	/** just a logger */
	private static final Logger LOG = Logger.getLogger(AbstractM2MMessageHandler.class);
	
	/** the next in line */
	protected M2MMessageHandler next;

	/** the type of message the handler can deal with */
	protected Class inType;
	
	/** the type of message the handler will send */
	protected Class outType;

	/** the message we are dealing with */
	protected M2MMessage message;
	
	/** enforce to skip doChain() */
	private boolean cont = true;
	
	/** Constructor */
	public AbstractM2MMessageHandler() {
		// lazy
	}
	
	/** Constructor */
	public AbstractM2MMessageHandler(M2MMessageHandler next, String inTypeClassName, String outTypeClassName) {
		try {
			this.next = next;
			this.inType = Class.forName(inTypeClassName);
			this.outType = Class.forName(outTypeClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

	/** 
	 * template method implementation
	 */	
	public final <T extends M2MMessage> void onMessageEvent(T message) throws M2MCommunicationException {
		
		this.setMessage(message);
		
		LOG.trace("checking messagetype");
		this.checkInMessageType();
		
		LOG.trace("handling message");
		this.handleMessage(message);
		
		if(null != this.next && cont) {
			LOG.trace("chaining");
			this.checkOutMessageType();
			this.doChain(this.message);	
		}
		else if (!cont){
			LOG.debug("Waiting to continue");
		}
		else if (null == this.next){
			LOG.debug("End of chain");	
		}
	}

	/** this needs to be overwritten ... individual handling of message */
	protected abstract <T extends M2MMessage> void handleMessage(T message);

	/** checking 4 correct type */
	private void checkInMessageType() throws M2MCommunicationException {
		// it is important to make use of getters instead of direct access to field as otherwise
		// CGLIB proxies used in AOP will provide null values for the fields
		if(! this.getInType().isInstance(this.getMessage())) {
			throw new M2MCommunicationException("MessageType can't be handeled by adapter");
		}
	}
	
	/** checking 4 correct type */
	private void checkOutMessageType() throws M2MCommunicationException {
		// it is important to make use of getters instead of direct access to field as otherwise
		// CGLIB proxies used in AOP will provide null values for the fields
		if(! this.getOutType().isInstance(this.getMessage())) {
			throw new M2MCommunicationException("MessageType can't be handeled by adapter");
		}
	}
	
	/**
	 * going for the next one ...
	 * @param message
	 * @throws MMSCommunicationException
	 */
	protected final <T extends M2MMessage> void doChain(T message) throws M2MCommunicationException {
		this.next.onMessageEvent(message);
	}

	
	// getter / setter enforced by Spring DI
	
	protected final M2MMessageHandler getNext() {
		return next;
	}

	protected final void setNext(M2MMessageHandler next) {
		this.next = next;
	}

	protected boolean isCont() {
		return cont;
	}

	protected void setCont(boolean cont) {
		this.cont = cont;
	}

	public M2MMessage getMessage() {
		return message;
	}

	public void setMessage(M2MMessage message) {
		this.message = message;
	}

	public Class getInType() {
		return inType;
	}

	public void setInType(Class inType) {
		this.inType = inType;
	}

	public Class getOutType() {
		return outType;
	}

	public void setOutType(Class outType) {
		this.outType = outType;
	}
}
