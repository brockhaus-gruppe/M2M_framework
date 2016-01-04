package de.brockhaus.m2m.config.aspect;

public class Foo {
	
	public <T> T doFoo(T msg) {
		System.out.println("Message in: " + msg);
		return msg;
	}
}
