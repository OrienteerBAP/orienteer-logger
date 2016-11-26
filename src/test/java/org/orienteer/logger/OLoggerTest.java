package org.orienteer.logger;

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
}
