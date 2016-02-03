package de.brockhaus.m2m.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.message.M2MMessageHandler;

/**
 * This is a stack which holds the handler chain as defined within the stack config xml file.
 * As Spring seems to instantiate the beans from the end to the top, we need to realize this as a LIFO stack. 
 * Currently this is NOT used and NOT configured by Spring DI (therefore it's a 'traditional' Singleton).
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 15, 2016
 *
 */
public class HandlerChainHolder {
	
	private static final Logger LOG = Logger.getLogger(HandlerChainHolder.class);
	
	private List<M2MMessageHandler> stack = new ArrayList<M2MMessageHandler>();
	
	private int stackPos = 0;
	
	// the initial value
	private int cursor = 1;
	// are we through?
	private boolean gotAll;
	
	
	public HandlerChainHolder() {

	}
	
	public void addHandler(M2MMessageHandler handler) {
		if(null != handler) {
			LOG.debug("adding 2 stack: " + handler.getClass().getSimpleName());
			this.stack.add(stackPos, handler);
			stackPos++;	
			LOG.debug("stack size: " + stack.size());
		}
	}
	
	public M2MMessageHandler getNextHandler() {
		
		M2MMessageHandler next = null;
		
		if(! this.gotAll) {

			if(cursor != this.stack.size()) {
				next = this.stack.get(cursor);
				cursor++;
			} else {
				this.gotAll = true;
			}
		}
		
		if(null != next) {
			LOG.debug("returning: " + next.getClass().getSimpleName());
		} else {
			LOG.debug("all elements retrieved, returning null");
		}
		
		return next;
	}
	
	private void init() {
		StringBuffer buf = new StringBuffer();
		for (M2MMessageHandler m2mMessageHandler : stack) {
			buf.append(" -> ");
			buf.append(m2mMessageHandler.getClass().getSimpleName());
			
			
		}
		LOG.debug("Stack is composed of: " + stack.size() + " elements: " + buf.toString());
	}
	
	public void reset() {
		this.cursor = 0;
		this.gotAll = false;
	}

	public List<M2MMessageHandler> getStack() {
		return stack;
	}

	public void setStack(List<M2MMessageHandler> stack) {
		this.stack = stack;
	}
}
