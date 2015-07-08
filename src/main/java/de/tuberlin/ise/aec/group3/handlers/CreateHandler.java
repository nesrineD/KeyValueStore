package de.tuberlin.ise.aec.group3.handlers;

import java.util.HashMap;
import java.util.HashSet;

import de.tuberlin.ise.aec.group3.KeyValueStore;
import de.tuberlin.ise.aec.group3.ReplicationTarget;
import de.tuberlin.ise.aec.group3.Replicator;
import de.tuberlin.ise.aec.group3.exceptions.KeyAlreadyExistsException;
import de.tuberlin.ise.aec.group3.exceptions.KeyNotFoundException;
import de.tuberlin.ise.aec.group3.exceptions.SynchronousReplicationFailedException;
import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

public class CreateHandler implements IRequestHandler {
	
	/** Map of all senders */
	private HashMap<String,Sender> senders;
	
	/** Map of replication targets for the specific node */
	private HashMap<String,HashSet<ReplicationTarget>> replicationTargets;
	
	/**
	 * Constructor for the CreateHandler
	 * @param senders
	 * @param replicationTargets
	 */
	public CreateHandler(HashMap<String,Sender> senders, HashMap<String,HashSet<ReplicationTarget>> replicationTargets) {
		this.senders = senders;
		this.replicationTargets = replicationTargets;
	}
	
	@Override
	public Response handleRequest(Request request) {
		
		/** Create a new entry in the key-value-store */
		String key = (String) request.getItems().get(0);
		byte[] value = (byte[]) request.getItems().get(1);
		Response resp = new Response("", false, request);

		try {
			KeyValueStore.getInstance().create(key, value);
			
			/** Forward the request to all other nodes according to ReplicationPathConfiguration */
			Replicator.replicate(request, replicationTargets, senders);
			
			resp = new Response(KeyValueStore.getInstance().read(key), "Result for Create: ", true, request);
		} catch (KeyAlreadyExistsException e) {
			return new Response("[KeyAlreadyExists]: " + e.getMessage(), false, request);
		} catch (SynchronousReplicationFailedException e) {
			return new Response("Result for replication: ", false, request);
		} catch (KeyNotFoundException e) {
			return new Response("[KeyNotFoundException]: " + e.getMessage(), false, request);
		}
			
		return resp;
	}

	
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
