package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class InvItemException extends Exception {
	
	public InvItemException() {
		super("Inventory Item error.");
	}
	public InvItemException(String msg) {
		super(msg);
	}
}
