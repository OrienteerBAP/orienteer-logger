package org.orienteer.inclogger.server;

import org.orienteer.inclogger.core.interfaces.IReceiver;
import org.orienteer.inclogger.core.interfaces.IServer;

public class DefaultReceiver implements IReceiver{

	IServer server;
	
	public DefaultReceiver() {
	}

	public void setServer(IServer server) {
		this.server=server;
	}
	
	public void receive(String clientInfo,String data) {
		server.onReceive(clientInfo,data);
	}

}
