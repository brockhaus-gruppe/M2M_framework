package de.brockhaus.m2m.receiver.file;

/**
 * 
 * Project: communication
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 14, 2015
 *
 */
public enum FileEvent
{
    FILE_READ(1), FILE_SEND(2);
    
    private int value;

    private FileEvent(int value) 
    {
            this.value = value;
    }
    
    public int getValue()
    {
    	return this.value;
    }
}
