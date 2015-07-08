package de.tub.aec.server;

import java.util.HashMap;
import java.util.HashSet;

import de.tuberlin.ise.aec.group3.callables.AsyncTargetCallable;
import de.tuberlin.ise.aec.group3.callables.QuorumTargetCallable;
import de.tuberlin.ise.aec.group3.callables.SyncTargetCallable;
import de.tuberlin.ise.aec.group3.exceptions.SynchronousReplicationFailedException;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

public class Replicator {	
	/**
	 * Forwards the request to all other nodes, aside from read operations.
	 * 
	 * @param request - Request to be propagated
	 * @param replicationTargets - Defines to whom the request needs to be forwarded.
	 * @param senders - all senders aside from the node itself.
	 * @throws SynchronousReplicationFailedException 
	 */
	public static void replicate(Request request, HashMap<String,HashSet<ReplicationTarget>> replicationTargets, HashMap<String,Sender> senders) throws SynchronousReplicationFailedException {
		/** Only propagate, if the node has any targets. */
		if (replicationTargets.containsKey(request.getOriginator())) {
			String originator = request.getOriginator();
			
			/** Set contains all synchronous, asynchronous and quorum targets. */
			HashSet<ReplicationTarget> set = replicationTargets.get(originator);
			
			/** Iterate over the targets */
			for (ReplicationTarget target : set) {

				/** For all asynchronous targets... */
				if(target.getType().equals("async")) {
					for(String s : target.getTargets()) {
						
						/** ...send messages asynchronously using the specific senders. */
						new AsyncTargetCallable(request, senders.get(s)).call();
					}
				
				/** For all synchronous targets... */
				} else if (target.getType().equals("sync")) {
					int countResp = 0;
					for(String s : target.getTargets()) {
						@SuppressWarnings("unused")
						Response resp = new SyncTargetCallable(request, senders.get(s)).call();
						countResp += 1;
					}
					
					/**.. if not, indicate that. */
					if (countResp != target.getTargetSize()) {
						throw new SynchronousReplicationFailedException();
					}
				
				/** For all quorum targets... */	
				} else if (target.getType().equals("quorum")) {
					
					/** ... first, find the right senders... */
					HashSet<Sender> quorumSenders = new HashSet<Sender>();
					for(String s : target.getTargets()) {
						quorumSenders.add(senders.get(s));
					}
					
					/** ... then start the quorum process */
					new QuorumTargetCallable(request, quorumSenders, target.getQsize()).call();
				}
			}
		}
	}
}
