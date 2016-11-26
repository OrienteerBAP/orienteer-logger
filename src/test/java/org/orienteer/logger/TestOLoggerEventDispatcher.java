package org.orienteer.logger;

import java.util.LinkedList;
import java.util.List;

public class TestOLoggerEventDispatcher implements IOLoggerEventDispatcher {

	private LinkedList<OLoggerEvent> events = new LinkedList<>();
	
	@Override
	public void dispatch(OLoggerEvent event) {
		events.add(event);
	}
	
	public List<OLoggerEvent> getEvents() {
		return events;
	}
	
	public void clear() {
		events.clear();
	}

}
