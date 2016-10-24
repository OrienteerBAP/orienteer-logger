package org.orienteer.inclogger.core;

import org.orienteer.inclogger.client.DefaultLogger;
import org.orienteer.inclogger.client.DefaultLoggerData;
import org.orienteer.inclogger.client.DefaultSender;
import org.orienteer.inclogger.core.interfaces.ICoder;
import org.orienteer.inclogger.core.interfaces.IConfigurator;
import org.orienteer.inclogger.core.interfaces.IData;
import org.orienteer.inclogger.core.interfaces.IDecoder;
import org.orienteer.inclogger.core.interfaces.ILogger;
import org.orienteer.inclogger.core.interfaces.IReceiver;
import org.orienteer.inclogger.core.interfaces.ISender;
import org.orienteer.inclogger.server.DefaultReceiver;

/**
 * 
 * 
 */
public class DefaultConfigurator implements IConfigurator {
	
	IData serverData;
	IData clientData;
	DefaultSender sender;
	IReceiver receiver;
	
	public DefaultConfigurator() {
		serverData = new DefaultData();
		clientData = new DefaultData();
		sender = new DefaultSender();
		receiver = new DefaultReceiver();
		sender.setReceiver(receiver);
	}

	public ICoder getCoder() {
		// TODO Auto-generated method stub
		return null;
	}

	public IDecoder getDecoder() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISender getSender() {
		return sender;
	}

	public IReceiver getReceiver() {
		return receiver;
	}

	public IData getServerData() {
		return serverData;
	}

	public IData getClientData() {
		return clientData;
	}
	
	public ILogger makeLogger() {
		 
		return new DefaultLogger(new DefaultLoggerData());
	}

}
