package de.tub.aec.server.callables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

public class QuorumTargetCallable implements Callable<Response>{

	private final Request request;
	private final HashSet<Sender> senders;
	private final int qsize;
	
	public QuorumTargetCallable(Request request, HashSet<Sender> senders, int qsize) {
		this.request = request;
		this.senders = senders;
		this.qsize = qsize;
	}
	@Override
	public Response call() {
		
		/** Create a thread pool with the number of targets. */
		ExecutorService executor = Executors.newCachedThreadPool();
		
		/** Future list where future responses are stored. */
		ArrayList<Future<Response>> list = new ArrayList<Future<Response>>();

		/** Iterate over senders. */
		for (Sender sender : senders) {
			/** Add the future response for each AsyncCallable to the list. */
			Callable<Response> worker = new SyncTargetCallable(request, sender);
			Future<Response> submit = executor.submit(worker);
			list.add(submit);
		}

		/** Wait for positive responses until qsize is met.
		 * TODO endlos Schleife falls niemals erreicht ?! vielleicht n timer einbauen
		 */
		ArrayList<Response> responses = new ArrayList<Response>();
		
		long timeoutExpiredMs = System.currentTimeMillis() + 25000;
		while ((responses.size() < this.qsize) && (System.currentTimeMillis() <= timeoutExpiredMs)) {
			for (Future<Response> future : list) {
				Response resp;
				try {
					resp = future.get();
				} catch (InterruptedException | ExecutionException e) {
					resp = new Response("", false, null);
				}

				if (resp.responseCode()) {
					responses.add(resp);
				}
			}
		}
		
		/** Once qsize is reached, give a positive response */
		Response response = new Response("Result for quorum: ", true, request);
		return response;

	}

}
