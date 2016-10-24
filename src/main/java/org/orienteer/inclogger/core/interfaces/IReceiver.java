package org.orienteer.inclogger.core.interfaces;

/**
 * 
 * 
 */
public interface IReceiver {

	void setServer(IServer server);

	void receive(String data);

}
