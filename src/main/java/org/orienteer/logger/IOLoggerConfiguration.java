package org.orienteer.logger;

import java.util.Map;

public interface IOLoggerConfiguration {
	
	String getApplicationName();
	void setApplicationName(String applicationName);

	String getNodeId();
	void setNodeId(String nodeId);

	String getCollectorUrl();
	void setCollectorUrl(String url);

	Object getProperty(String name);
	void setProperty(String name, Object value);

	Map<String, ?> getProperties();
	void setProperties(Map<String, ?> properties);

	IOCorrelationIdGenerator getCorrelationIdGenerator();
	void setCorrelationIdGenerator(IOCorrelationIdGenerator correlationIdGenerator);
}
