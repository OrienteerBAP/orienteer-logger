package ru.asm.utils.incident.logger.core;


public abstract class AbstractLogger implements ILogger{

	protected ILoggerData<?> data;
	IClient client;
	
	public AbstractLogger(ILoggerData<?> data) {
		this.data = data;
	}

	public ILoggerData<?> getData() {
		return data;
	}
	
	//add record and send all records to server
	public void incident(String incident){
	    writeData(incident);
		client.onIncident(this);
	}
	
	//just record without sending
	public void message(String message){
	    writeData(message);
	}
	
	protected abstract void writeData(String message);
	
	public void setClient(IClient client){
		assert(client != null);
		this.client = client;
	}
	
	public void unlinkClient(){
		this.client.removeLogger(this);
	}
}
