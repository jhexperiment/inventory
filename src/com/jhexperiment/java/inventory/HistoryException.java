package com.jhexperiment.java.inventory;

@SuppressWarnings("serial")
public class HistoryException extends Exception {
	
	public HistoryException() {
		super("History error.");
	}
	public HistoryException(String msg) {
		super(msg);
	}
}
