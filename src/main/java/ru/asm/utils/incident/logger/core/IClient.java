package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public interface IClient {

	public void onIncident(ILogger logger);
	public void addLogger(ILogger logger);
	public void removeLogger(ILogger logger);

}
