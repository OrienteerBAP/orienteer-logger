package ru.asm.utils.incident.logger.core;

import ru.asm.utils.incident.logger.IncidentLogger;

public class DefaultSender implements ISender{

	IReciever reciever; 
	
	public DefaultSender() {
		// TODO Auto-generated constructor stub
	}
	
	public void setReciever(IReciever reciever){
		this.reciever = reciever;
	}

	public void send(String input) {
		if(reciever != null){
			reciever.recieve(input);
		}
	}

}
