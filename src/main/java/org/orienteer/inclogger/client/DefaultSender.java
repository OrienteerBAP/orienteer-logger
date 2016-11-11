package org.orienteer.inclogger.client;

import org.orienteer.inclogger.IncidentLogger;
import org.orienteer.inclogger.core.interfaces.IReceiver;
import org.orienteer.inclogger.core.interfaces.ISender;

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
			receiver.receive("default",input);
			return true;
		}
		return false;
	}

}
