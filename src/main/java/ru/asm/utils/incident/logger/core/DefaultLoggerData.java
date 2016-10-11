package ru.asm.utils.incident.logger.core;

public class DefaultLoggerData implements ILoggerData{
	
	String data;

	public DefaultLoggerData() {
		data = "";
	}
	
	public void add(String data){
		this.data = this.data.concat(data);
	}
	
	public String get(){
		return data;
	}

	public void clear() {
		data = "";
	}
}
