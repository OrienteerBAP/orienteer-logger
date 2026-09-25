package org.orienteer.logger;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class OLoggerTest {

	@Test
	public void testGenericOLogger() {
		OLogger.log("Hello world");
		OLogger.log(new Exception("Test stack trace"));
		Exception exc = new Exception("Test correlation ID");
		OLogger.log(exc);
		OLogger.log(exc);
	}
	
	@Test
	public void testExceptionCorelation() {
		TestOLoggerEventDispatcher dispatcher = new TestOLoggerEventDispatcher();
		OLogger logger = new OLoggerBuilder().setLoggerEventDispatcher(dispatcher).addDefaultEnhancers().create();
		logger.logEvent("Hello world");
		logger.logEvent("Hello world");
		logger.logEvent(new StringBuffer("Hello world"));
		assertEquals(3, dispatcher.getEvents().size());
		assertEquals(dispatcher.getEvents().get(0).getCorrelationId(), dispatcher.getEvents().get(1).getCorrelationId());
		assertNotEquals(dispatcher.getEvents().get(0).getCorrelationId(), dispatcher.getEvents().get(2).getCorrelationId());
		dispatcher.clear();
		Exception exc = new Exception("Test correlation ID");
		// Its important to have those exceptions in one row
		Exception exc2 = new Exception("Test correlation ID"); Exception exc3 = new Exception("Test correlation ID #3");
		logger.logEvent(exc);
		logger.logEvent(exc);
		logger.logEvent(exc2);
		logger.logEvent(exc3);
		assertEquals(4, dispatcher.getEvents().size());
		assertEquals(dispatcher.getEvents().get(0).getCorrelationId(), dispatcher.getEvents().get(1).getCorrelationId());
		assertNotEquals(dispatcher.getEvents().get(0).getCorrelationId(), dispatcher.getEvents().get(2).getCorrelationId());
		assertEquals(dispatcher.getEvents().get(2).getCorrelationId(), dispatcher.getEvents().get(3).getCorrelationId());
	}
	
	@Test
	public void testSourceAndMetaData() {
		TestOLoggerEventDispatcher dispatcher = new TestOLoggerEventDispatcher();
		OLogger logger = new OLoggerBuilder().setLoggerEventDispatcher(dispatcher).addDefaultEnhancers().create();
		Map<String, Object> metaData = new HashMap<>();
		metaData.put("customKey", "customValue");
		logger.logEvent("Hello world", "TestSource", metaData);
		logger.logEvent("Hello world");
		assertEquals(2, dispatcher.getEvents().size());
		
		OLoggerEvent event = dispatcher.getEvents().get(0);
		assertEquals("TestSource", event.getSource());
		assertEquals("customValue", event.getMetaData("customKey"));
		String json = event.toJson();
		assertTrue(json, json.contains("\"source\" : \"TestSource\""));
		assertTrue(json, json.contains("\"customKey\" : \"customValue\""));
		
		OLoggerEvent plain = dispatcher.getEvents().get(1);
		assertNull(plain.getSource());
		assertTrue(plain.getMetaData().isEmpty());
		// Source and metadata don't take part in the correlation ID
		assertEquals(event.getCorrelationId(), plain.getCorrelationId());
	}
	
	@Test
	public void testStaticLogWithSourceAndMetaData() {
		TestOLoggerEventDispatcher dispatcher = new TestOLoggerEventDispatcher();
		OLogger.set(new OLoggerBuilder().setLoggerEventDispatcher(dispatcher).addDefaultEnhancers().create());
		try {
			Map<String, Object> metaData = new HashMap<>();
			metaData.put("customKey", 42);
			OLogger.log(new Exception("Test source"), "TestSource", metaData);
			OLogger.log(new Exception("Test null source"), null, null);
			assertEquals(2, dispatcher.getEvents().size());
			assertEquals("TestSource", dispatcher.getEvents().get(0).getSource());
			assertEquals(42, dispatcher.getEvents().get(0).getMetaData("customKey"));
			assertNull(dispatcher.getEvents().get(1).getSource());
			assertTrue(dispatcher.getEvents().get(1).getMetaData().isEmpty());
		} finally {
			OLogger.set(null);
		}
	}
}
