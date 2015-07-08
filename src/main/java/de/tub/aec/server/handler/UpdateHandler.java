package de.tub.aec.server.handler;

import java.util.HashMap;
import java.util.HashSet;

import de.tub.aec.server.KeyValueStore;
import de.tub.aec.server.ReplicationTarget;
import de.tub.aec.server.Replicator;
import de.tub.aec.server.exceptions.KeyNotFoundException;
import de.tub.aec.server.exceptions.SameValueException;
import de.tub.aec.server.exceptions.SynchronousReplicationFailedException;
import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

public class UpdateHandler implements IRequestHandler {

	/** Map of all senders */
	private HashMap<String,Sender> senders;
	
	/** Map of replication targets for the specific node */
	private HashMap<String,HashSet<ReplicationTarget>> replicationTargets;

	/**
	 * Constructor for the UpdateHandler
	 * @param senders
	 * @param replicationTargets
	 */
	public UpdateHandler(HashMap<String,Sender> senders, HashMap<String,HashSet<ReplicationTarget>> replicationTargets) {
		this.senders = senders;
		this.replicationTargets = replicationTargets;
	}
	
	@Override
	public Response handleRequest(Request request) {
		
		/** Update the key-value-store using the key and the new value */
		String key = (String) request.getItems().get(0);
		byte[] value = (byte[]) request.getItems().get(1);
		
		Response resp = new Response("", false, request);
		try {
			KeyValueStore.getInstance().update(key, value);

			/** Forward the request to all other nodes according to ReplicationPathConfiguration */
			Replicator.replicate(request, replicationTargets, senders);
			
			resp = new Response(KeyValueStore.getInstance().read(key), "Result for Update: ", true, request);
		} catch(SynchronousReplicationFailedException e) {
			return new Response("Result for replication: ", false, request);
		} catch (KeyNotFoundException e) {
			return new Response("[KeyNotFoundException]: " + e.getMessage(), false, request);
		} catch (SameValueException e) {
			return new Response("[SameValueException] Nothing to replicate: " + e.getMessage(), true, request);
		}
		
		return resp;
	
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
