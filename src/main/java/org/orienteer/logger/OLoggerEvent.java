package org.orienteer.logger;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static org.orienteer.logger.OLoggerUtils.*;

public class OLoggerEvent {
	
	/**
	 * Seed is being used only by enrichers: it's not recommended to do something with it in dispatcher
	 */
	private Object seed;
	private String eventId = UUID.randomUUID().toString();
	private String message;
	private String application;
	private String nodeId;
	private Date dateTime = new Date();
	private String correlationId = eventId;
	private Map<String, Object> metaData = new HashMap<>();
	private Object source;
	
	public Object getSeed() {
		return seed;
	}

	public void setSeed(Object seed) {
		this.seed = seed;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
	public Map<String, Object> getMetaData() {
		return Collections.unmodifiableMap(metaData);
	}
	
	public Object getMetaData(String key) {
		return metaData.get(key);
	}
	
	public OLoggerEvent setMetaData(String key, Object value) {
		metaData.put(key, value);
		return this;
	}
	
	public OLoggerEvent setMetaData(Map<String, ?> meta) {
		metaData.putAll(meta);
		return this;
	}
	
	public Object getSource() {
		return source;
	}
	
	public OLoggerEvent setSource(Object source) {
		this.source = source;
		return this;
	}
	
	public String toJson() {
		StringBuilder sb = new StringBuilder("{");
			appendJson(sb, "eventId", this.eventId).append(", ");
			appendJson(sb, "correlationId", this.correlationId).append(", ");
			appendJson(sb, "application", this.application).append(", ");
			appendJson(sb, "nodeId", this.nodeId).append(", ");
			appendJson(sb, "message", this.message).append(", ");
			for(Map.Entry<String, Object> entry : metaData.entrySet()) {
				appendJson(sb, entry.getKey(), entry.getValue()).append(", ");
			}
			appendJson(sb, "source", this.source).append(", ");
			appendJson(sb, "dateTime", this.dateTime.getTime());
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
}
