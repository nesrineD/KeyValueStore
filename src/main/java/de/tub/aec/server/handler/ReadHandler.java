package de.tub.aec.server.handler;

import de.tub.aec.server.KeyValueStore;
import de.tub.aec.server.exceptions.KeyNotFoundException;
import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;

public class ReadHandler implements IRequestHandler {

	@Override
	public Response handleRequest(Request request) {
		String key = (String) request.getItems().get(0);
		
		try {
			return new Response(KeyValueStore.getInstance().read(key), "Result for Read: ", true, request);
		} catch (KeyNotFoundException e) {
			return new Response("[KeyNotFoundException]: " + e.getMessage(), false, request);
		}	
	}

	@Override
	public boolean hasPriority() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requiresResponse() {
		// TODO Auto-generated method stub
		return true;
	}

}
