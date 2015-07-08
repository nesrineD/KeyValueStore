package de.tuberlin.ise.aec.group3.handlers;

import de.tuberlin.ise.aec.group3.KeyValueStore;
import de.tuberlin.ise.aec.group3.exceptions.KeyNotFoundException;
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
