package org.orienteer.inclogger.core.interfaces;

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
	
	//add exception and send all records to server
	public void incident(Throwable e);

	//just finalize current record without sending
	public void message(String message);
}
