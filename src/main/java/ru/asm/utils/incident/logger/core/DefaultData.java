package ru.asm.utils.incident.logger.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ru.asm.utils.incident.logger.core.IData.IDataFlag;

/**
 * 
 * 
 */
public class DefaultData implements IData {
	private Map<Integer,String> data;
	private Integer counter = 0;

	public DefaultData() {
		data = new HashMap<Integer,String>();
	}
	
	public void applyLoggerData(ILoggerData<?> loggerData) {
		this.data.put(counter++, (String) loggerData.get());
	}

	public String getData() {
		return getData(IDataFlag.NOTHING);
	}

	public String getData(IDataFlag flag) {
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
	
	public void mark(IDataFlag before,IDataFlag now){
		// TODO Auto-generated method stub
	}

	public void applyData(String newData) {
		// TODO Auto-generated method stub
		
	}

}
