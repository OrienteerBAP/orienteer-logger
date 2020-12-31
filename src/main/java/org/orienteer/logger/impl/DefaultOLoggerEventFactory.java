package org.orienteer.logger.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

import org.orienteer.logger.IOLoggerConfigurable;
import org.orienteer.logger.IOLoggerConfiguration;
import org.orienteer.logger.IOLoggerEventFactory;
import org.orienteer.logger.OLoggerEvent;
import static org.orienteer.logger.OLoggerUtils.*;

public class DefaultOLoggerEventFactory implements IOLoggerEventFactory, IOLoggerConfigurable {
	
	private IOLoggerConfiguration config;

	@Override
	public OLoggerEvent createLoggerEvent(Object seedObject, Object source, Map<String, ?> metaData) {
		OLoggerEvent event;
		if (seedObject instanceof OLoggerEvent) {
			event = (OLoggerEvent) seedObject;
		} else {
			event = new OLoggerEvent();
			if(seedObject instanceof Throwable) {
				event.setMessage(createMessageFromThrowable((Throwable) seedObject));
			} else if (seedObject != null) {
				event.setMessage(createMessageFromObject(seedObject));
			}
			event.setSeed(seedObject);
		}
		event.setApplication(config.getApplicationName());
		event.setNodeId(config.getNodeId());
		event.setCorrelationId(createCorrelationId(seedObject));
		return event;
	}

	protected String createMessageFromThrowable(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

	protected String createMessageFromObject(Object seedObject) {
		return seedObject.toString();
	}

	protected String createCorrelationId(Object seedObject) {
		return config.getCorrelationIdGenerator().generate(seedObject);
	}

	@Override
	public void configure(IOLoggerConfiguration configuration) {
		this.config = configuration;
	}


}
