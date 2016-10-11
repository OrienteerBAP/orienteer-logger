package ru.asm.utils.incident.logger.core;

public class DefaultLogger implements ILogger{

	ILoggerData data;
	IClient client;
	
	public DefaultLogger(ILoggerData data) {
		this.data = data;
	}

	public ILoggerData getData() {
		return data;
	}
	
	public void incident(String incident){
		data.add(incident);
		client.onIncident(this);
		
	}
	
	public void setClient(IClient client){
		this.client = client;
	}

}
