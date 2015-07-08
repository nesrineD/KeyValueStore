package de.tuberlin.ise.aec.group3.exceptions;

public class SameValueException extends Exception {

	private static final long serialVersionUID = 1L;

	public SameValueException() {
		super();
	}
	
	public SameValueException(String msg) {
		super(msg);
	}

}
