package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public class DefaultLoggerData implements ILoggerData<String>{
	
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

	public void set(String name, String value) {
		this.data = this.data.concat(name+":"+value+"\n");
	}

	public void end() {
		this.data = this.data.concat("\n\n");
	}
}
