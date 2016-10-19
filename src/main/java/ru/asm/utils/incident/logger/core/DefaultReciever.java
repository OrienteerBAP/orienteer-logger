package ru.asm.utils.incident.logger.core;

public class DefaultReciever implements IReciever{

	IServer server;
	
	public DefaultReciever() {
	}

	public void setServer(Server server) {
		this.server=server;
	}
	
	public void recieve(String data) {
		server.onRecieve(data);
	}

}
