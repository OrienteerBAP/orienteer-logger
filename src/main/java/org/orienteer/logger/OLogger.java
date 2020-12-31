package org.orienteer.logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Entry point for Orienteer Logger
 */
public class OLogger {
	
	private final IOLoggerEventFactory loggerEventFactory;
	private final List<IOLoggerEventEnhancer> enahancers;
	private final IOLoggerEventDispatcher loggerEventDispatcher;
	
	OLogger(IOLoggerEventFactory loggerEventFactory, List<IOLoggerEventEnhancer> enahancers, IOLoggerEventDispatcher loggerEventDispatcher) {
		this.loggerEventFactory = loggerEventFactory;
		this.enahancers = Collections.unmodifiableList(enahancers);
		this.loggerEventDispatcher = loggerEventDispatcher;
	}
	
	public void logEvent(Object object) {
		logEvent(object, null, null);
	}
	
	public void logEvent(Object object, Object source) {
		logEvent(object, source, null);
	}
	
	public void logEvent(Object object, Object source, Map<String, ?> metaData) {
		OLoggerEvent event = loggerEventFactory.createLoggerEvent(object, source, metaData);
		for (IOLoggerEventEnhancer enhancer : enahancers) {
			event = enhancer.enhance(event);
		}
		loggerEventDispatcher.dispatch(event);
	}
	
	
	private static OLogger defaultInstance;
	
	public static OLogger get() {
		if(defaultInstance==null) {
			defaultInstance = new OLoggerBuilder()
									.addDefaultEnhancers()
									.create(); 
		}
		return defaultInstance;
	}
	
	public static void set(OLogger oLogger) {
		defaultInstance = oLogger;
	}
	
	public static void log(Object object) {
		get().logEvent(object);
	}
	
	public static void log(Object object, Object source) {
		get().logEvent(object, source);
	}
	
	public static void log(Object object, Object source, Map<String, ?> metaData) {
		get().logEvent(object, source, metaData);
	}
	
}
