package com.jhexperiment.java.inventory;

/**
 * Exception used to inform that the absence being performed on is a duplicate.
 * @author jhxmonkey
 *
 */
@SuppressWarnings("serial")
public class DuplicateHistoryException extends Exception {
	
	public DuplicateHistoryException() {
	    super("Duplicate history.");
	}
	
	public DuplicateHistoryException(String msg) {
	    super(msg);
	}
	
}
