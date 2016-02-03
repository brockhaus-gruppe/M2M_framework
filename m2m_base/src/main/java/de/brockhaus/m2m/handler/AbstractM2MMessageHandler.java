package de.brockhaus.m2m.handler;

import java.text.MessageFormat;

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
	private M2MMessageHandler next;

	/** the type of message the handler can deal with */
	private Class<?> inType;
	
	/** the type of message the handler will send */
	private Class<?> outType;

	/** the message we are dealing with */
	private M2MMessage message;
	
	/** enforce to skip doChain() */
	private boolean continueProceeding = true;
	
	/** the handler chain which determines the sequence */
	private HandlerChainHolder handlerChain;
	
	/** Constructor */
	public AbstractM2MMessageHandler() {
		// lazy
	}
	
	/** Constructor */
	public AbstractM2MMessageHandler(String inTypeClassName, String outTypeClassName) {
		try {
			this.setInType(Class.forName(inTypeClassName));
			this.setOutType(Class.forName(outTypeClassName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

	/** 
	 * template method implementation
	 */	
	public final <T extends M2MMessage> void onMessageEvent(T message) throws M2MCommunicationException {
		// can we deal with incoming message?
		this.checkInMessageType(message);	
		
		//setting the message to the instance
		this.setMessage(message);
		
		// hand over to subclass
		this.handleMessage(message);
		
		// setting the follower within the chain
		M2MMessageHandler follower = this.getHandlerChain().getNextHandler();
		if(null != follower) {
			this.setNext(follower);	
		}
		
		// we will continue if ...
		if(null != this.getNext() && this.getContinueProceeding()) {
			this.checkOutMessageType(this.getMessage());
			this.doChain(this.getMessage());	
		}
		
		// stopping
		else if (! this.getContinueProceeding()){
			LOG.debug("Waiting to continue");
		}
		
		// termination of chain
		else if (null == this.next){
			LOG.debug("End of chain");
		}
	}

	/** this needs to be overwritten ... individual handling of message by subclass */
	public abstract <T extends M2MMessage> void handleMessage(T message);

	/** checking 4 correct inbound message type */
	private  <T extends M2MMessage> void checkInMessageType(T message) throws M2MCommunicationException {
		LOG.trace("checking incoming messagetype: " + message.getClass().getSimpleName());

		// it is important to make use of getters instead of direct access to field as otherwise
		// CGLIB proxies used in AOP will provide null values for the fields
		if(! this.getInType().isInstance(message)) {
			Object[] data = {this.getClass().getSimpleName(), message.getClass().getSimpleName(), this.getInType().getSimpleName()};
			MessageFormat form = new MessageFormat("{0} -> inbound MessageType: {1} can not be handeled by adapter, should be: {2},  check config");
			throw new M2MCommunicationException(form.format(data));
		}
	}
	
	/** checking 4 correct outbound messagetype */
	private <T extends M2MMessage> void checkOutMessageType(T message) throws M2MCommunicationException {
		LOG.trace("checking outgoing messagetype: "  + message.getClass().getSimpleName());
		
		// it is important to make use of getters instead of direct access to field as otherwise
		// CGLIB proxies used in AOP will provide null values for the fields
		if(! this.getOutType().isInstance(message)) {
			Object[] data = {this.getClass().getSimpleName(), message.getClass().getSimpleName(), this.getOutType().getSimpleName()};
			MessageFormat form = new MessageFormat("{0}-> outbound MessageType: {1} can not be handeled by adapter, should be: {2}, check config");
			throw new M2MCommunicationException(form.format(data));
		}
	}
	
	/**
	 * going for the next one ...
	 * @param message
	 * @throws MMSCommunicationException
	 */
	protected final <T extends M2MMessage> void doChain(T message) throws M2MCommunicationException {
		LOG.debug("chaining");
		this.getNext().onMessageEvent(message);
	}

	
	// getter / setter enforced by Spring DI, all must be public
	public final M2MMessageHandler getNext() {
		// might happen if doChain is invoked without previous onMessageEvent
		if(null == next) {
			next = this.handlerChain.getNextHandler();
		}
		return next;
	}

	public final void setNext(M2MMessageHandler next) {
		this.next = next;
	}

	public boolean getContinueProceeding() {
		return continueProceeding;
	}

	public void setContinueProceeding(boolean stopProceeding) {
		this.continueProceeding = stopProceeding;
	}

	public M2MMessage getMessage() {
		return message;
	}

	public void setMessage(M2MMessage message) {
		this.message = message;
	}

	public Class<?> getInType() {
		return inType;
	}

	public void setInType(Class<?> inType) {
		this.inType = inType;
	}

	public Class<?> getOutType() {
		return outType;
	}

	public void setOutType(Class<?> outType) {
		this.outType = outType;
	}

	public HandlerChainHolder getHandlerChain() {
		return handlerChain;
	}

	public void setHandlerChain(HandlerChainHolder handlerChain) {
		this.handlerChain = handlerChain;
	}	
}
