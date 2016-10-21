package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */

public class Server implements IServer{
	private IData data;
	private IReceiver receiver;
	private IDecoder decoder;

	public Server(IConfigurator configurator) {
		decoder = configurator.getDecoder();
		receiver = configurator.getReceiver();
		if (receiver!=null) receiver.setServer(this);
		data = configurator.getServerData();
	}
	
	public void onReceive(String receivedData){
		if (decoder!=null){
			receivedData = decoder.decode(receivedData);
		}
		data.applyData(receivedData);
	}
	
	public IData getData(){
		return data;
	}
	
}
