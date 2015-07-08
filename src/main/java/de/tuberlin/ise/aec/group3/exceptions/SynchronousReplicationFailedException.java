package de.tuberlin.ise.aec.group3.exceptions;

public class SynchronousReplicationFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public SynchronousReplicationFailedException() {
		super();
	}
	
	public SynchronousReplicationFailedException(String msg) {
		super(msg);
	}
}
