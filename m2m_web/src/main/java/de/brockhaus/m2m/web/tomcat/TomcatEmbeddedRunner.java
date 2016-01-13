package de.brockhaus.m2m.web.tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * 
 * https://tomcat.apache.org/maven-plugin-trunk/executable-war-jar.html
 * http://www.hostettler.net/blog/2012/04/09/embedded-jee-web-application-integration-testing-using-tomcat-7/
 * 
 * Project: integration_web
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, May 19, 2015
 *
 */
public class TomcatEmbeddedRunner {

	/** The tomcat instance. */
	private Tomcat tCat;
	
	/** The temporary directory in which Tomcat and the app are deployed. */
	private String mWorkingDir = System.getProperty("java.io.tmpdir");
	
	private String warFileLocation;
	
	private int port;

	public TomcatEmbeddedRunner() {
		// lazy
	}
	
	// get the things rolling
	private void init() {
	
		tCat = new Tomcat();
		tCat.setPort(port);
		tCat.setBaseDir(mWorkingDir);
		tCat.getHost().setAppBase(mWorkingDir);
		tCat.getHost().setAutoDeploy(true);
		tCat.getHost().setDeployOnStartup(true);
		tCat.enableNaming();
		
		File webApp = new File(warFileLocation);
		Context ctx = tCat.addWebapp(tCat.getHost(), "/sensorData", webApp.getAbsolutePath());		
	}
	
	public void startServer() {
		try {
			tCat.start();
			tCat.getServer().await();
		} catch (LifecycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getWarFileLocation() {
		return warFileLocation;
	}

	public void setWarFileLocation(String warFileLocation) {
		this.warFileLocation = warFileLocation;
	}
}
