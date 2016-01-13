package de.brockhaus.m2m.web;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;

/**
 * CURRENTLY THIS IS NOT WORKING DUE TO JERSEY/WELD problems
 * 
 * https://tomcat.apache.org/maven-plugin-trunk/executable-war-jar.html
 * http://www
 * .hostettler.net/blog/2012/04/09/embedded-jee-web-application-integration
 * -testing-using-tomcat-7/
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
	
	private String warFile = "/home/mbohnen/Desktop/sensorData.war";

	public static void main(String[] args) throws LifecycleException {
		TomcatEmbeddedRunner runner = new TomcatEmbeddedRunner();
		runner.startServer();
	}

	public void startServer() throws LifecycleException {
	
		tCat = new Tomcat();
		tCat.setPort(8080);
		tCat.setBaseDir(mWorkingDir);
		tCat.getHost().setAppBase(mWorkingDir);
		tCat.getHost().setAutoDeploy(true);
		tCat.getHost().setDeployOnStartup(true);
		tCat.enableNaming();
		
		File webApp = new File(warFile);
		Context ctx = tCat.addWebapp(tCat.getHost(), "/sensorData", webApp.getAbsolutePath());		
		
		tCat.start();
		tCat.getServer().await();
	}

}
