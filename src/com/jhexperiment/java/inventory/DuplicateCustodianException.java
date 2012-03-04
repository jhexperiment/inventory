package com.jhexperiment.java.inventory;

/**
 * Exception used to inform that the absence being performed on is a duplicate.
 * @author jhxmonkey
 *
 */
@SuppressWarnings("serial")
public class DuplicateCustodianException extends Exception {
	
	public DuplicateCustodianException() {
	    super("Duplicate custodian.");
	}
	
	public DuplicateCustodianException(String msg) {
	    super(msg);
	}
	
}
