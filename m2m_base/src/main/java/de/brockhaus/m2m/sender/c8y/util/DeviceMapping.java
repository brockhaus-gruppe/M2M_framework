package de.brockhaus.m2m.sender.c8y.util;

import java.io.Serializable;

/**
 * Mapping for one device, e.g.
 * 
 * tagId = 0, 
 * tagName = Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor conveyer belt swap
 * gid = 10991
 * 
 * m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author jperez, 22.02.2016
 *
 */
public class DeviceMapping implements Serializable {
	
	private String gid;
	private String tagName;
	private int tagId;
	
	public DeviceMapping(String gid, String tagName, int tagId) {
		this.gid = gid;
		this.tagName = tagName;
		this.tagId = tagId;
		
	}
	
	public String  getGid()
	{
		return this.gid;
	}
	
	public void setGid(String gid)
	{
		this.gid = gid;
	}
	
	public String getTagName()
	{
		return this.tagName;
	}
	
	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}
	
	public int getTagId()
	{
		return this.tagId;
	}
	
	public void setTagId(int tagId)
	{
		this.tagId = tagId;
	}
}