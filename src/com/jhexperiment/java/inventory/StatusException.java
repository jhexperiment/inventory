package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class StatusException extends Exception {
	
	public StatusException() {
		super("Status error.");
	}
	public StatusException(String msg) {
		super(msg);
	}
}
