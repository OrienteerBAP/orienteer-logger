package org.orienteer.inclogger.server;

import org.orienteer.inclogger.core.interfaces.IConfigurator;
import org.orienteer.inclogger.core.interfaces.IData;
import org.orienteer.inclogger.core.interfaces.IDecoder;
import org.orienteer.inclogger.core.interfaces.IReceiver;
import org.orienteer.inclogger.core.interfaces.IServer;

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
	
	public void onReceive(String clientInfo, String receivedData){
		if (decoder!=null){
			receivedData = decoder.decode(receivedData);
		}
		data.applyData(clientInfo,receivedData);
	}
	
	public IData getData(){
		return data;
	}
	
}
