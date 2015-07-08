package de.tuberlin.ise.aec.group3.callables;

import java.util.concurrent.Callable;

import edu.kit.aifb.dbe.hermes.AsyncCallbackRecipient;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

public class AsyncTargetCallable implements AsyncCallbackRecipient, Callable<Response> {

	/** Original Request */
	private final Request request;
	
	/** To whom the request should be forwarded */
	private final Sender sender;
	
	public AsyncTargetCallable(Request request, Sender sender) {
		this.request = request;
		this.sender = sender;
	}
	
	/**
	 * Transmits the data, i.e. request, to a different node specified with the sender.
	 */
	@Override
	public Response call() { 
		boolean succeeded = sender.sendMessageAsync(request, this);
		Response response = new Response("Result for Async Message: ", succeeded, request);
		
		return response;
	}

	@Override
	public void callback(Response arg0) {
		// TODO Auto-generated method stub
		
	}
}
