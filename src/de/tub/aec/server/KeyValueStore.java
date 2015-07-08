package de.tub.aec.server;

import java.util.HashMap;

import org.apache.log4j.Logger;

import de.tub.aec.server.exceptions.KeyAlreadyExistsException;
import de.tub.aec.server.exceptions.KeyNotFoundException;
import de.tub.aec.server.exceptions.SameValueException;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

public class KeyValueStore {
	
	private static Logger logger = Logger.getLogger(KeyValueStore.class);
	
	long writeTime;

	/** Singleton instance */
	private static KeyValueStore instance;
	
	/** The actual key-value store */
	private HashMap<String, byte[]> store = new HashMap<String, byte[]>();
	
	/**
	 * Make sure that only one instance exists.
	 *
	 * @return the singleton
	 */
	public static KeyValueStore getInstance() {
		if (KeyValueStore.instance == null) {
			KeyValueStore.instance = new KeyValueStore();
		}
		return instance;
	}
	
	/**
	 * Add a new value to the store.
	 *
	 * @param key
	 * @param value
	 * @throws KeyAlreadyExistsException
	 */
	public void create(String key, byte[] value)
			throws KeyAlreadyExistsException {
		/** Key does not yet exists, entry can be added */
		if (!store.containsKey(key)) {
			store.put(key, value);
		} else {
			throw new KeyAlreadyExistsException("Key already exists in the store.");
		}
	}
	
	/**
	 * Read a value from the store
	 *
	 * @param key
	 * @return the value
	 * @throws KeyNotFoundException
	 */
	public byte[] read(String key)
			throws KeyNotFoundException {
		/** Check, if the key exists in the store */
		if (store.containsKey(key)) {
			return store.get(key);
		} else {
			throw new KeyNotFoundException("Key does not exist in the store.");
		}
	}
	
	/**
	 * Delete a value from the store.
	 *
	 * @param key
	 * @throws KeyNotFoundException
	 */
	public void delete(String key)
			throws KeyNotFoundException {
		/** Check, if the key exists in the store */
		if (store.containsKey(key)) {
			store.remove(key);
		} else {
			throw new KeyNotFoundException("Key does not exist in the store.");
		}
	}

	/**
	 * @param host ip address to which the client is reachable
	 * @param port port number of the client
	 * @param req
	 * @return
	 */
	private static Response sendMessage(String host, int port, Request req) {
		Sender s = new Sender(host, port);
		return s.sendMessage(req, 1000);
	}
	
	/**
	 * Update an old value with a new one.
	 *
	 * @param key
	 * @param value
	 * @throws KeyNotFoundException
	 * @throws SameValueException
	 */
	public void update(String key, byte[] value)
			throws KeyNotFoundException, SameValueException {
		/** Check, if the key exists in the store */
		if (store.containsKey(key)) {
			if (!store.get(key).equals(value)) {
				store.replace(key, value);
				writeTime = System.currentTimeMillis();
				logger.info("the write time is" + writeTime);
				// send the time of the write to the client
				Request req = new Request("writeTime", "nodeD");
				req.addItem(writeTime);
				Response resp = sendMessage("52.18.96.133", 6000, req);
				logger.info("the response is to D" + resp + "message sent");
				
			} else {
				throw new SameValueException("Value has not changed.");
			}
		} else {
			throw new KeyNotFoundException("Key does not exist in the store.");
		}
	}
}
