package de.brockhaus.m2m.aspect;

/**
 * A stupid class with the sole purpose to be wrapped by an aspect
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 28, 2015
 *
 */
public class Foo {
	
	private String bar;
	
	public Foo(String bar) {
		this.bar = bar;
	}
	
	public String doFoo(String msg) {
		System.out.println("Message in: " + msg);
		return (msg + bar).toUpperCase();
	}

	public String getBar() {
		return bar;
	}

	public void setBar(String bar) {
		this.bar = bar;
	}
}
