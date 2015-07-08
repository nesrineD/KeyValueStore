package de.tuberlin.ise.aec.group3;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.aifb.dbe.hermes.Sender;

public class ParseMappingTest {
	
	public static Map<String, Sender> map;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		map = Helper.parseMapping();
	}

//	@Before
//	public void setUp() throws Exception {
//	}

	@Test
	public void testLength() {
		assertEquals(3, map.size());
	}

}
