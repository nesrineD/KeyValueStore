package de.tub.aec.server.exceptions;

public class KeyNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public KeyNotFoundException() {
		super();
	}
	
	public KeyNotFoundException(String msg) {
		super(msg);
	}

}
