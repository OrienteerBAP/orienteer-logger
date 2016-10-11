package ru.asm.utils.incident.logger.core;

public class DefaultConfigurator implements IConfigurator {
	
	IData data;
	
	public DefaultConfigurator() {
		data = new DefaultData();
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
		// TODO Auto-generated method stub
		return null;
	}

	public IReciever getReciever() {
		// TODO Auto-generated method stub
		return null;
	}

	public IData getServerData() {
		return data;
	}

	public IData getClientData() {
		return data;
	}
	
	public ILogger makeLogger() {
		 
		return new DefaultLogger(new DefaultLoggerData());
	}

}
