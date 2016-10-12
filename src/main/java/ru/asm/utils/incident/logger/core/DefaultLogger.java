package ru.asm.utils.incident.logger.core;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 */
public class DefaultLogger implements ILogger{

	ILoggerData<?> data;
	IClient client;
	
	static SimpleDateFormat ft =  new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ssXXX");//w3c datetime format
	
	public DefaultLogger(ILoggerData<?> data) {
		this.data = data;
	}

	public ILoggerData<?> getData() {
		return data;
	}
	
	//add record and send all records to server
	public void incident(String incident){
	    writeData(incident);
		client.onIncident(this);
	}
	
	//just record without sending
	public void message(String message){
	    writeData(message);
	}
	
	protected void writeData(String message){
	    Package objPackage = this.getClass().getPackage(); 
	    
	    String appname = objPackage.getSpecificationTitle();
	    String appver = objPackage.getSpecificationVersion();
	      
	    data.set("Application", appname+ " v"+appver);
	    data.set("DateTime", ft.format(new Date()));
	    data.set("UserName", System.getProperty("user.name"));
	    data.set("Data", message);
	    data.end();
	}
	
	public void setClient(IClient client){
		assert(false);
		assert(client == null);
		this.client = client;
	}
	
	public void unlinkClient(){
		this.client.removeLogger(this);
	}

}
