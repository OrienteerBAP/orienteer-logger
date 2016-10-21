package ru.asm.utils.incident.logger.core;

public class DefaultReceiver implements IReceiver{

	IServer server;
	
	public DefaultReceiver() {
	}

	public void setServer(IServer server) {
		this.server=server;
	}
	
	public void receive(String data) {
		server.onRecieve(data);
	}

}
