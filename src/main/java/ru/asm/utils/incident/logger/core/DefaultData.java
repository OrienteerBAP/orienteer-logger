package ru.asm.utils.incident.logger.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.asm.utils.incident.logger.core.IData.IDataFlag;

/**
 * 
 * 
 */
public class DefaultData implements IData {
	private Map<Integer,String> data;
	private Integer counter = 0;
	private Gson gson = new Gson();


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
		return gson.toJson(data);
	}
	
	public void mark(IDataFlag before,IDataFlag now){
		if (before ==IDataFlag.SENDED && now == IDataFlag.SENDED_SUCCESSFULLY){
				
		}
	}

	public void applyData(String newData) {
		Map<Integer,String> anotherData = gson.fromJson(newData, new TypeToken<HashMap<Integer,String>>(){}.getType());
		for(Entry<Integer, String> entry : anotherData.entrySet()) {
			if (!data.containsKey(entry.getKey())){
				data.put(entry.getKey(), entry.getValue());
			}
		}		
	}

}
