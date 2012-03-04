package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class InvalidGeneralItemException extends Exception {
	
	public InvalidGeneralItemException() {
		super("Invalid General Item. Missing required fields.");
	}
	public InvalidGeneralItemException(String msg) {
		super(msg);
	}
}
