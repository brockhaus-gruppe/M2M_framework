package de.brockhaus.m2m.sender.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MPlainTextMessage;
import de.brockhaus.m2m.sender.M2MSendingWorker;

/**
 * Sending through http, the receiving adapter might be a RESTful service, a servlet ...
 * check here: http://www.vogella.com/tutorials/ApacheHttpClient/article.html
 * 
 * Example config:
 * 
	<!-- sending through http -->
	<bean name="http-sender"
		class="de.brockhaus.m2m.sender.http.HTTPSendingWorker" scope="singleton">

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MPlainTextMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MPlainTextMessage</value>
    	</constructor-arg>
    	
    	<!-- who deals with the file once content is sent -->
		<property name="url">
			<value>http://localhost:8080/sensorData/sensorDataReceiver</value>
		</property>
	
	</bean>	
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 11, 2015
 *
 */
public class HTTPSendingWorker extends AbstractM2MMessageHandler implements M2MSendingWorker {
	
	private static final Logger LOG = Logger.getLogger(HTTPSendingWorker.class);
	
	// the url to send to
	private String url; 

	public HTTPSendingWorker() {
		super();
	}

	public HTTPSendingWorker(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		this.doSend(message);
	}
	
	@Override
	public void doSend(M2MMessage message) {
		
		HttpClient client = null;
		
		try {
			client =  HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			
			String json = ((M2MPlainTextMessage) message).getSensordata();
			
			// adding values to the request
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("sensor_data", json));
 
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(post);
			
			LOG.debug("\nSending 'POST' request to URL : " + url);
			LOG.debug("Post parameters : " + post.getEntity());
			LOG.debug("Response Code : " +  response.getStatusLine().getStatusCode());
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
