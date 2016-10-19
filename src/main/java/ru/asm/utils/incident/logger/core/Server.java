package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */

public class Server implements IServer{
	private IData data;
	private IReciever reciever;
	private IDecoder decoder;

	public Server(IConfigurator configurator) {
		decoder = configurator.getDecoder();
		reciever = configurator.getReciever();
		if (reciever!=null) reciever.setServer(this);
		data = configurator.getServerData();
	}
	
	public void onRecieve(String recievedData){
		if (decoder!=null){
			recievedData = decoder.decode(recievedData);
		}
		data.applyData(recievedData);
	}
	
	public IData getData(){
		return data;
	}
	
}
