package org.orienteer.inclogger.client;

import org.orienteer.inclogger.core.interfaces.ILoggerData;

/**
 * 
 * 
 */
public class DefaultLoggerData implements ILoggerData<String>{
	
	String data;

	public DefaultLoggerData() {
		data = "";
	}

	public String get(){
		return data;
	}

	public void clear() {
		data = "";
	}

	public DefaultLoggerData set(String name, String value) {
		this.data = this.data.concat(name+":"+value+"\n");
		return this;
	}

	public void end() {
		this.data = this.data.concat("\n\n");
	}
}
