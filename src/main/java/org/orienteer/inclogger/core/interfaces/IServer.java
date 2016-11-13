package org.orienteer.inclogger.core.interfaces;

/**
 * 
 * 
 */
public interface IServer {

	public IData getData();
	public void onReceive(String clientInfo, String receivedData);
}
