package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public class DefaultConfigurator implements IConfigurator {
	
	IData serverData;
	IData clientData;
	DefaultSender sender;
	IReciever reciever;
	
	public DefaultConfigurator() {
		serverData = new DefaultData();
		clientData = new DefaultData();
		sender = new DefaultSender();
		reciever = new DefaultReciever();
		sender.setReciever(reciever);
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

	public IReciever getReciever() {
		return reciever;
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
