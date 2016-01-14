package de.brockhaus.m2m.config.aspect;

/**
 * pretty simple aop test
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 14, 2016
 *
 */
public class Foo {
	
	public <T> T doFoo(T msg) {
		System.out.println("Message in: " + msg);
		return msg;
	}
}
