package de.brockhaus.m2m.receiver.file;

import java.io.File;

/**
 * There might be implementations for XML files, JSON files, or proprietary formats like the one used 
 * by DÜRR IOManager ...
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 12, 2015
 *
 */
public interface FileEventHandler
{
	public void handleFile(File file, FileEvent event, FileHandlerCallback callback);
}