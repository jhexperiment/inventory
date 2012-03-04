package com.jhexperiment.java.inventory;

/**
 * Exception used to inform that the absence being performed on is a duplicate.
 * @author jhxmonkey
 *
 */
@SuppressWarnings("serial")
public class DuplicateInvItemException extends Exception {
	
	public DuplicateInvItemException() {
	    super("Duplicate Inventory Item.");
	}
	
	public DuplicateInvItemException(String msg) {
	    super(msg);
	}
	
}
