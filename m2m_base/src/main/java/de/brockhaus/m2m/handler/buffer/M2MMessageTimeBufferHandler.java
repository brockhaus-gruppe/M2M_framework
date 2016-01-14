package de.brockhaus.m2m.handler.buffer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * TODO: check http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/CacheBuilder.html
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 18, 2015
 *
 */
public class M2MMessageTimeBufferHandler extends AbstractM2MMessageHandler implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(M2MMessageTimeBufferHandler.class);

	private List<M2MSensorMessage> buffer = new ArrayList<M2MSensorMessage>();

	private M2MMultiMessage multiMessage;
	
	
	public M2MMessageTimeBufferHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public M2MMessageTimeBufferHandler(M2MMessageHandler next, String inTypeClassName, String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {	
		
		LOG.debug("adding to bufer");
		this.buffer.add((M2MSensorMessage) message);
		
		// avoid chaining right now
		super.setContinueProceeding(true);
		
		// nothing else will happen
	}
	
	protected void onTimeout() {
		LOG.debug("timeout occured");
		
		// only if something is buffered
		if(this.buffer.size() > 0) {
			multiMessage = new M2MMultiMessage();
			multiMessage.setSensorDataMessageList(buffer);
			this.setMessage(multiMessage);
			try {
				LOG.debug("chaining");
				super.doChain(multiMessage);
				
				LOG.debug("Flushing buffer");
				this.buffer.clear();
			} catch (M2MCommunicationException e) {
				e.printStackTrace();
			}	
		}	
	}

	@Override
	public void run() {
		this.onTimeout();
	}


	@Override
	protected void finalize() throws Throwable {
		LOG.debug("chaining");
		this.doChain(multiMessage);
	}
	
	
}
