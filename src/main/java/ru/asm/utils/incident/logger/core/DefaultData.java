package ru.asm.utils.incident.logger.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultData implements IData {
	private Map<Integer,String> data;
	private Integer counter = 0;

	public DefaultData() {
		data = new HashMap<Integer,String>();
	}
	
	public void applyLoggerData(ILoggerData loggerData) {
		this.data.put(counter++, loggerData.get());
	}

	public String get() {
		// TODO serialize
		
		String result = "";
		for(Entry<Integer, String> entry : data.entrySet()) {
			result = result.concat(entry.getKey().toString())
				.concat("-")
				.concat(entry.getValue())
				.concat("\n");
		}
		return result;
	}

	public void apply(String newData) {
		// TODO unserialize and apply new objects
		
	}

}
