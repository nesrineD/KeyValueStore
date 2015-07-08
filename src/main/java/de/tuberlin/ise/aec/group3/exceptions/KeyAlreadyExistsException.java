package de.tuberlin.ise.aec.group3.exceptions;

public class KeyAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public KeyAlreadyExistsException() {
		super();
	}
	
	public KeyAlreadyExistsException(String msg) {
		super(msg);
	}

}
