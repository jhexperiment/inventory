package com.jhexperiment.java.inventory;

/**
 * Exception used to inform that the absence being performed on is a duplicate.
 * @author jhxmonkey
 *
 */
@SuppressWarnings("serial")
public class DuplicateStatusException extends Exception {
	
	public DuplicateStatusException() {
	    super("Duplicate status.");
	}
	
	public DuplicateStatusException(String msg) {
	    super(msg);
	}
	
}
