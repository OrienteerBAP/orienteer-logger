package org.orienteer.logger.impl;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.orienteer.logger.IOLoggerConfiguration;

public class DefaultOLoggerConfiguration implements IOLoggerConfiguration {
	
	private String mainClassName;
	
	
	private Map<String, Object> properties = new HashMap<>();
	
	public DefaultOLoggerConfiguration() {
		Properties sysProperties = System.getProperties();
		for(Object key : sysProperties.keySet()) {
			if(key instanceof String) {
				String name = (String) key;
				if(name.startsWith("ologger.")) {
					setProperty(name.substring("ologger.".length()), sysProperties.get(name));
				}
			}
		}
	}
	
	public DefaultOLoggerConfiguration(Map<String, ?> properties) {
		setProperties(properties);
	}

	@Override
	public String getApplicationName() {
		String application = valueOf(getProperty("application"));
		if(application==null && mainClassName==null) {
			Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
			for(StackTraceElement[] stackTrace : stackTraces.values()) {
				if(stackTrace==null || stackTrace.length==0) continue;
				StackTraceElement candidate = stackTrace[stackTrace.length-1];
				if("main".equals(candidate.getMethodName())) {
					mainClassName = candidate.getClassName();
					break;
				}
			}
		}
		return application!=null?application:mainClassName;
	}

	@Override
	public void setApplicationName(String applicationName) {
		setProperty("application", applicationName);
	}

	@Override
	public String getNodeId() {
		String nodeId =  valueOf(getProperty("nodeId"));
		if(nodeId==null) nodeId = ManagementFactory.getRuntimeMXBean().getName();
		return nodeId;
	}

	@Override
	public void setNodeId(String nodeId) {
		setProperty("nodeId", nodeId);
	}

	@Override
	public String getCollectorUrl() {
		return valueOf(getProperty("collectorUrl"));
	}

	@Override
	public void setCollectorUrl(String url) {
		setProperty("collectorUrl", url);
	}

	@Override
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}

	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public void setProperties(Map<String, ?> properties) {
		this.properties.putAll(properties);
	}

	@Override
	public Map<String, ?> getProperties() {
		return Collections.unmodifiableMap(properties);
	}
	
	private static String valueOf(Object object) {
		return object!=null?object.toString():null;
	}
	
}
