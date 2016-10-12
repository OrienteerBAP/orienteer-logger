package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public interface ILogger {

	public ILoggerData<?> getData();
	
	//autocall
	public void setClient(IClient client);
	
	//should be called before program is successfully closed 
	public void unlinkClient();

	//add record and send all records to server
	public void incident(String incident);

	//just make record without sending
	public void message(String message);
	
}
