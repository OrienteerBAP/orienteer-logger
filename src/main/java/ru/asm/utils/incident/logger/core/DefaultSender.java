package ru.asm.utils.incident.logger.core;

import ru.asm.utils.incident.logger.IncidentLogger;

public class DefaultSender implements ISender{

	IReceiver receiver; 
	
	public DefaultSender() {
		// TODO Auto-generated constructor stub
	}
	
	public void setReceiver(IReceiver receiver){
		this.receiver = receiver;
	}

	public boolean send(String input) {
		if(receiver != null){
			receiver.receive(input);
			return true;
		}
		return false;
	}

}
