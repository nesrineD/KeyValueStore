package de.tub.aec.server.callables;

import java.util.concurrent.Callable;

import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

public class SyncTargetCallable implements Callable<Response> {

	/** Original Request */
	private final Request request;
	
	/** To whom the request should be forwarded */
	private final Sender sender;
	
	public SyncTargetCallable(Request request, Sender sender) {
		this.request = request;
		this.sender = sender;
	}
	
	/**
	 * Transmits the data, i.e. request, to a different node specified with the sender.
	 */
	@Override
	public Response call() {
		Response response = sender.sendMessage(request, 200);
		return response;
	}

}
