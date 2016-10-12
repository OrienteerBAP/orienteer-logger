package ru.asm.utils.incident.logger.core;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 
 */
public class Client implements IClient{
	private Set<ILogger> loggers;
	private IData data;
	private ISender sender;
	private ICoder coder;

	public Client(IConfigurator configurator) {
		coder = configurator.getCoder();
		sender = configurator.getSender();
		data = configurator.getClientData();

		loggers = new HashSet<ILogger>();
	}
	
	public void addLogger(ILogger logger){
		loggers.add(logger);
		logger.setClient(this);
	}
	
	public void removeLogger(ILogger logger){
		loggers.remove(logger);
	}
	
	public void onIncident(ILogger logger){
		data.applyLoggerData(logger.getData());
		sendData();
		logger.getData().clear();
	}
	
	private void sendData(){
		String toSend = data.get();
		if (coder != null ){
			toSend = coder.code(toSend);
		}
		if (sender != null){
			sender.send(toSend);
		}
	}
}
