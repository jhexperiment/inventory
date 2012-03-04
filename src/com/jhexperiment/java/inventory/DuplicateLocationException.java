package com.jhexperiment.java.inventory;

/**
 * Exception used to inform that the absence being performed on is a duplicate.
 * @author jhxmonkey
 *
 */
@SuppressWarnings("serial")
public class DuplicateLocationException extends Exception {
	
	public DuplicateLocationException() {
	    super("Duplicate location.");
	}
	
	public DuplicateLocationException(String msg) {
	    super(msg);
	}
	
}
