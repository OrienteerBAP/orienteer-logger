package org.orienteer.logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.orienteer.logger.impl.DefaultOLoggerConfiguration;
import org.orienteer.logger.impl.DefaultOLoggerEventDispatcher;
import org.orienteer.logger.impl.DefaultOLoggerEventFactory;

public class OLoggerBuilder {
	
	private IOLoggerEventFactory loggerEventFactory = new DefaultOLoggerEventFactory();
	private IOLoggerEventDispatcher loggerEventDispatcher = new DefaultOLoggerEventDispatcher();
	private List<IOLoggerEventEnhancer> enhancers = new ArrayList<>();
	
	public OLoggerBuilder setLoggerEventFactory(IOLoggerEventFactory loggerEventFactory) {
		this.loggerEventFactory = loggerEventFactory;
		return this;
	}

	public OLoggerBuilder setLoggerEventDispatcher(IOLoggerEventDispatcher loggerEventDispatcher) {
		this.loggerEventDispatcher = loggerEventDispatcher;
		return this;
	}

	public OLoggerBuilder setEnhancers(List<IOLoggerEventEnhancer> enhancers) {
		this.enhancers = enhancers;
		return this;
	}
	
	public OLoggerBuilder addEnhancer(IOLoggerEventEnhancer enhancer) {
		this.enhancers.add(enhancer);
		return this;
	}
	
	public OLoggerBuilder clearEnanhcers() {
		this.enhancers.clear();
		return this;
	}
	
	public OLoggerBuilder addDefaultEnhancers() {
		return this;
	}
	
	public OLogger create() {
		return create(new DefaultOLoggerConfiguration());
	}

	public OLogger create(IOLoggerConfiguration configuration) {
		OLogger ret = new OLogger(configure(loggerEventFactory, configuration), 
								  configure(enhancers, configuration), 
								  configure(loggerEventDispatcher, configuration));
		return ret;
	}
	
	private static <T> T configure(T entity, IOLoggerConfiguration configuration) {
		if(entity instanceof IOLoggerConfigurable){
			((IOLoggerConfigurable)entity).configure(configuration);
		} else if(entity instanceof Collection<?>) {
			for(Object subEntity : (Collection<?>)entity) {
				configure(subEntity, configuration);
			}
		}
		return entity;
	}
}
