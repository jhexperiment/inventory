package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class InvalidElectronicItemException extends Exception {
	
	public InvalidElectronicItemException() {
		super("Invalid Electronic Item. Missing required fields.");
	}
	public InvalidElectronicItemException(String msg) {
		super(msg);
	}
}
