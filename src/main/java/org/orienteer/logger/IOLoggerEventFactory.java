package org.orienteer.logger;

import java.util.Map;

public interface IOLoggerEventFactory {
	public OLoggerEvent createLoggerEvent(Object seedObject, Object source, Map<String, ?> metaData);
}
