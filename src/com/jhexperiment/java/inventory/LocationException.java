package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class LocationException extends Exception {
	
	public LocationException() {
		super("Location error.");
	}
	public LocationException(String msg) {
		super(msg);
	}
}
