package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class InvalidKeyItemException extends Exception {
	
	public InvalidKeyItemException() {
		super("Invalid Key Item. Missing required fields.");
	}
	public InvalidKeyItemException(String msg) {
		super(msg);
	}
}
