package org.orienteer.inclogger.core.interfaces;

/**
 * 
 * 
 */
public interface IClient {

	public void onIncident(ILogger logger);
	public void addLogger(ILogger logger);
	public void removeLogger(ILogger logger);

}
