package de.brockhaus.m2m.receiver.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessageReceiverLifecycle;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MRddMessage;

/**
 * Monitoring the file system for changes. 
 * Once a file comes in, it will be checked whether it is of interest it will be read by a handler
 * and the messages will be created.
 * How we will deal with the file once we read it will be handled by another handler.
 * 
 * Config example:
 * 
	<!-- the adapter where everything starts -->
	<bean name="file_adapter" class="de.brockhaus.m2m.receiver.file.FileAdapter" init-method="init">
		
		<!-- the next handler in line, see below -->
		<constructor-arg ref="json_converter" />
		
		<!-- doing nothing 
		<constructor-arg name = "next">
			<null />
		</constructor-arg>
		 -->
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MSensorMessage</value>
    	</constructor-arg>
		
		
		<!-- The file names we're interested in and the sensor type assigned to it -->
		<property name="fileNamePatterns">
			<map>
				<entry key="PT_DS1_316233.ED01_AB219_M04.AS.V2251_Setpoint.csv" value="PT_DS1_316233.ED01_AB219_M04.AS.V2251" />
				<entry key="PT_DS1_316233.ED01_AB219_M04.AS.V2252_Setpoint.csv" value="PT_DS1_316233.ED01_AB219_M04.AS.V2252" />
				<entry key="PT_DS1_316233.ED01_AB219_M04.AS.V2253_Setpoint.csv" value="PT_DS1_316233.ED01_AB219_M04.AS.V2253" />
				<entry key="PT_DS1_316233.ED01_AB219_M04.AS.V2254_Setpoint.csv" value="PT_DS1_316233.ED01_AB219_M04.AS.V2254" />
				<entry key="PT_DS1_316233.ED01_FA011.AA.R2444_BalActValue.csv" value="PT_DS1_316233.ED01_FA011.AA.R2444" />
				<entry key="PT_DS1_316233.ED01_FA011.AA.R2445_BalActValue.csv" value="PT_DS1_316233.ED01_FA011.AA.R2445" />
			</map>
		</property>
				
		<!-- the directory to monitor -->
		<property name="directory">
			<value>/media/DataOne/work/projects/testAndTry/communicationStack/communication/src/test/resources/in/</value>
		</property>	
		
		<!-- who deals with the file once read -->
		<property name="fileReadHandler" ref = "file_read_handler" />
		
		<!-- who deals with the file once content is sent -->
		<property name="fileSendHandler">
			<null/>
		</property>
		
	</bean>
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Feb 3, 2016
 *
 */
public class FileAdapter extends AbstractM2MMessageHandler implements FileHandlerCallback, M2MMessageReceiverLifecycle {

	private static final Logger LOG = Logger.getLogger(FileAdapter.class);
	
	private FileEventHandler fileReadHandler;
	private FileEventHandler fileSendHandler;

	/** which filenames we should react on */
	private HashMap<String, String> fileNamePatterns;

	/** which directory to monitor */
	private String directory;

	private String fullFileName;

	private String fileName;

	private WatchService watchService;

	private WatchKey watchKey;

	public FileAdapter() {
		super();

	}

	public FileAdapter(String inTypeClassName,	String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		try {
			super.doChain(message);
		} catch (M2MCommunicationException e) {
			LOG.error(e);
		}
	}
	
	public void handleEventResult(Object... result) {
		
		// it's is presumed we get a M2MMultiMessage
		if(! (result[0] instanceof M2MMultiMessage)) {
			throw new RuntimeException("We can't handle this result");
		}
		this.handleMessage((M2MMultiMessage) result[0]);
	}
	
	private void init() {
		try {
			watchService = FileSystems.getDefault().newWatchService();

			File watchDirFile = new File(this.directory);
			Path watchDirPath = watchDirFile.toPath();

			// recursively registering all sub-directories (some magic) as
			// described here: http://codingjunkie.net/eventbus-watchservice/
			Files.walkFileTree(watchDirPath, new WatchServiceRegisteringVisitor());

			LOG.info("\n" 
					+ "******************************************************************************************"
					+ "\n" + this.getClass().getSimpleName() + " up'n'running" + " for: " + this.directory + "\n"
					+ "******************************************************************************************");

		} catch (IOException e) {
			LOG.error(e);
		}
	}
	
	public void start() {
		
		// registering the path for some events
		// use this WITHOUT sub-dirs and without inner class
		// WatchKey watchKey = watchDirPath.register(watchService,
		// StandardWatchEventKinds.ENTRY_CREATE);
		// watch this if you want to scan sub-directories
		watchKey = watchService.poll();

		// waiting for the things to happen ...
		try {
			watchKey = watchService.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true) {
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				LOG.debug("\n Event: " + event.kind() + " \n " + "File: " + event.context());

				// getting the needful information
				Path dir = (Path) watchKey.watchable();
				Path fullPath = dir.resolve(event.context().toString());
				fullFileName = fullPath.toString();
				fileName = event.context().toString();

				// go for it
				this.onNewFile(fileName);
			}
		}
	}
	
	/**
	 * triggered once the file shows up
	 * 
	 * @param fileName
	 * @throws MMSCommunicationException
	 */
	private void onNewFile(String fileName) {
		try {
			// are we interested in the file ?
			if (!this.fileNamePatterns.keySet().contains(fileName)) {
				LOG.info(fileName + " not of interest");
				return;
			}

			LOG.info("reading: " + fileName);

			// delegate to handler
			this.fileReadHandler.handleFile(new File(this.fullFileName), FileEvent.FILE_READ, this);

		} catch (Exception e) {
			LOG.error(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private String filterByName(String fileName) {
		String ret = null;

		for (String substring : this.fileNamePatterns.keySet()) {
			if (fileName.contains(substring)) {
				ret = substring;
				break;
			}
		}

		return ret;
	}
	

	public HashMap<String, String> getFileNamePatterns() {
		return fileNamePatterns;
	}

	public void setFileNamePatterns(HashMap<String, String> fileNamePatterns) {
		this.fileNamePatterns = fileNamePatterns;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public FileEventHandler getFileReadHandler() {
		return fileReadHandler;
	}

	public void setFileReadHandler(FileEventHandler fileReadHandler) {
		this.fileReadHandler = fileReadHandler;
	}

	public FileEventHandler getFileSendHandler() {
		return fileSendHandler;
	}

	public void setFileSendHandler(FileEventHandler fileSendHandler) {
		this.fileSendHandler = fileSendHandler;
	}
	
	/** 
	 * inner class recursively walking to all sub-directories to register them for being watched 
	 * check here: http://codingjunkie.net/eventbus-watchservice/
	 */
	private class WatchServiceRegisteringVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            return FileVisitResult.CONTINUE;
        }
    }

	@Override
	public void stop() {
		System.exit(0);	
	}
}
