package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public interface IReceiver {

	void setServer(IServer server);

	void receive(String data);

}
