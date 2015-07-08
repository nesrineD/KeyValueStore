package de.tuberlin.ise.aec.group3;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.aifb.dbe.hermes.Sender;
import edu.kit.aifb.dbe.hermes.SimpleFileLogger;

public class ReplicationPathConfigurationTest {
	
	public static String nodeA = "nodeA";
	public static String nodeB = "nodeB";
	public static String nodeC = "nodeC";
	
	public static HashMap<String,HashSet<ReplicationTarget>> listA;
	public static HashMap<String,HashSet<ReplicationTarget>> listB;
	public static HashMap<String,HashSet<ReplicationTarget>> listC;
	
	public static Map<String, Sender> map;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		
		listA = Helper.getReplicationTargets(nodeA);
		listB = Helper.getReplicationTargets(nodeB);
		listC = Helper.getReplicationTargets(nodeC);
	}

//	@Before
//	public void setUp() throws Exception {
//	}

	@Test
	public void testReplicationHashMapA() {
		System.out.println("List for NodeA");
		
		HashMap<String,HashSet<ReplicationTarget>> tempListA = new HashMap<String, HashSet<ReplicationTarget>>();
		ReplicationTarget sync = new ReplicationTarget("sync");
		sync.addTarget(nodeB);
		
		HashSet<ReplicationTarget> hashSet = new HashSet<ReplicationTarget>();
		hashSet.add(sync);
		
		tempListA.put("nodeA", hashSet);
		assertEquals(tempListA.size(), listA.size());

		Iterator it = listA.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey());
	        
	        Set<ReplicationTarget> set = (HashSet) pair.getValue();
	        
	        for(ReplicationTarget t : set) {
	        	System.out.println(t.getType() + " = " + t.getTargetSize());
	        }
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		System.out.println("-----");
	}
	
	@Test
	public void testReplicationHashMapB() {
		System.out.println("List for NodeB");
		
		HashMap<String,HashSet<ReplicationTarget>> tempListB = new HashMap<String, HashSet<ReplicationTarget>>();
		ReplicationTarget quorum = new ReplicationTarget("quorum");
		quorum.setQsize(1);
		quorum.addTarget(nodeA);
		quorum.addTarget(nodeC);
		
		ReplicationTarget async = new ReplicationTarget("async");
		async.addTarget(nodeC);
		
		HashSet<ReplicationTarget> hashSetStartA = new HashSet<ReplicationTarget>();
		hashSetStartA.add(async);
		tempListB.put("nodeA", hashSetStartA);
		
		HashSet<ReplicationTarget> hashSetStartB = new HashSet<ReplicationTarget>();  
		hashSetStartB.add(quorum);
		tempListB.put("nodeB", hashSetStartB);
		
		assertEquals(tempListB.size(), listB.size());
		
		Iterator it = listB.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey());
	        
	        Set<ReplicationTarget> set = (HashSet) pair.getValue();
	        
	        for(ReplicationTarget t : set) {
	        	System.out.println(t.getType() + " = " + t.getTargetSize());
	        }
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    System.out.println("-----");
	}
	
	@Test
	public void testReplicationHashMapC() {
		System.out.println("List for NodeC");
		Iterator it = listC.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey());
	        
	        Set<ReplicationTarget> set = (HashSet) pair.getValue();
	        
	        for(ReplicationTarget t : set) {
	        	System.out.println(t.getType() + " = " + t.getTargetSize());
	        }
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    System.out.println("--------");
	}
}
