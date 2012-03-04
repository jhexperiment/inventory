package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class CustodianException extends Exception {
	
	public CustodianException() {
		super("Custodian error.");
	}
	public CustodianException(String msg) {
		super(msg);
	}
}
