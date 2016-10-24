package org.orienteer.inclogger.client;

import java.util.HashSet;
import java.util.Set;

import org.orienteer.inclogger.core.interfaces.IClient;
import org.orienteer.inclogger.core.interfaces.ICoder;
import org.orienteer.inclogger.core.interfaces.IConfigurator;
import org.orienteer.inclogger.core.interfaces.IData;
import org.orienteer.inclogger.core.interfaces.ILogger;
import org.orienteer.inclogger.core.interfaces.ISender;
import org.orienteer.inclogger.core.interfaces.IData.IDataFlag;

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
		logger.getData().clear();
		if (sendData()){
			data.mark(IDataFlag.SENDED, IDataFlag.SENDED_SUCCESSFULLY);
		};
	}
	
	private boolean sendData(){
		if (sender != null){
			String toSend = data.getData(IDataFlag.SENDED);
			if (coder != null ){
				toSend = coder.code(toSend);
			}
			return sender.send(toSend);
		}
		return true;
	}
}
