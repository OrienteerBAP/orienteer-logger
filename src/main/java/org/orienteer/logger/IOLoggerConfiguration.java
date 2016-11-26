package org.orienteer.logger;

import java.util.Map;

public interface IOLoggerConfiguration {
	
	public String getApplicationName();
	public void setApplicationName(String applicationName);
	public String getNodeId();
	public void setNodeId(String nodeId);
	public String getCollectorUrl();
	public void setCollectorUrl(String url);
	public void setProperty(String name, Object value);
	public Object getProperty(String name);
	public void setProperties(Map<String, ?> properties);
	public Map<String, ?> getProperties();

}
