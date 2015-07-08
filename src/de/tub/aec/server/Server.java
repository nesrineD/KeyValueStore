package de.tub.aec.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import de.tub.aec.server.handler.CreateHandler;
import de.tub.aec.server.handler.DeleteHandler;
import de.tub.aec.server.handler.ReadHandler;
import de.tub.aec.server.handler.UpdateHandler;
import edu.kit.aifb.dbe.hermes.Receiver;
import edu.kit.aifb.dbe.hermes.RequestHandlerRegistry;
import edu.kit.aifb.dbe.hermes.Sender;
import edu.kit.aifb.dbe.hermes.SimpleFileLogger;

public class Server {
	
	private RequestHandlerRegistry reg;
	private Receiver receiver;
	private HashMap<String, Sender> senders;
	
	/**
	 * Unique ID for the server node.
	 */
	private String id;
	
	

	/**
	 * Stores the replication strategies for each node.
	 */
	private HashMap<String, HashSet<ReplicationTarget>> replicationTargets;

	/**
	 * Maximum number of threads allowed for both acceptThreads and
	 * processThreads.
	 */
	private final int MAX_THREADS = 10;

	/**
	 * Initialize the server.
	 * 
	 * @param id Unique name for the server. Assumed to be know for all
	 *        instances.
	 * @param port Under which port the server is accessible.
	 * @param acceptThreads Maximum of 10. If out of scope, it is automatically
	 *        set to the default value 3.
	 * @param processThreads Maximum of 10. If out of scope, it is automatically
	 *        set to the default value 3.
	 * @param ip 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public Server(String id, int port, int acceptThreads, int processThreads)
			throws IOException, ParserConfigurationException, SAXException {

		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		reg = RequestHandlerRegistry.getInstance();
		this.id = id;
		
        
		senders = Helper.parseMapping();
		replicationTargets = Helper.getReplicationTargets(this.id);

		/** Register the various handlers */
		reg.registerHandler("create", new CreateHandler(senders, replicationTargets));
		reg.registerHandler("read", new ReadHandler());
		reg.registerHandler("update", new UpdateHandler(senders, replicationTargets));
		reg.registerHandler("delete", new DeleteHandler(senders, replicationTargets));

		/** TODO necessary? */
//		 if (acceptThreads < this.MAX_THREADS && acceptThreads > 1 &&
//		 processThreads < this.MAX_THREADS && processThreads > 1) {
//		 this.receiver = new Receiver(port, acceptThreads, processThreads);
//		} else {
		receiver = new Receiver(port, 5, 5);
		//}

		/** Server starts to accept requests */
		receiver.start();
	}
}
