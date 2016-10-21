package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public interface IConfigurator {

	ICoder getCoder();// if return null - then we will translate raw data

	IDecoder getDecoder(); // should return null if we will translate raw data 

	ISender getSender(); //if return null - then all data be never sended

	IReceiver getReceiver(); //may be null only in case getSender return null

	IData getServerData();
	
	IData getClientData();
	
	ILogger makeLogger();
	
}
