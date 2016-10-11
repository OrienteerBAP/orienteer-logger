package ru.asm.utils.incident.logger.core;

public interface ILogger {

	public ILoggerData getData();

	public void setClient(IClient client);

	public void incident(String incident);
	
}
