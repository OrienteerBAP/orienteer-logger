package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public interface IReciever {

	void setServer(Server server);

	void recieve(String data);

}
