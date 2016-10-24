package org.orienteer.inclogger;

import java.util.ArrayList;
import java.util.List;

import org.orienteer.inclogger.client.Client;
import org.orienteer.inclogger.core.interfaces.IClient;
import org.orienteer.inclogger.core.interfaces.IConfigurator;
import org.orienteer.inclogger.core.interfaces.IData;
import org.orienteer.inclogger.core.interfaces.ILogger;
import org.orienteer.inclogger.core.interfaces.IServer;
import org.orienteer.inclogger.server.Server;

/**
 * Entry point for incident logger
 * 
 */
public class IncidentLogger {
	
	private static final IncidentLogger INSTANCE = new IncidentLogger();
	
	private IConfigurator configurator;
	private IClient client;
	private IServer server;
	
	private List<ILogger> loggers = new ArrayList<ILogger>();
	
	private IncidentLogger() {
	}
	
	/**
	 * Should be called before other loggers initialize
	 * @param configurator Your configurator. Should implement {@link IConfigurator} interface
	 */
	public static void init(IConfigurator configurator) {
		assert(INSTANCE.configurator == null);
		assert(configurator != null);
		
		INSTANCE.configurator = configurator;

		INSTANCE.client = new Client(configurator);
		INSTANCE.server = new Server(configurator);
	}
	
	/**
	 * Should be called before your program ends or you will be flooded with unnecessary messages. 
	 */
	public static void close(){
		if(INSTANCE.configurator != null){
			for(ILogger l :INSTANCE.loggers){
				l.unlinkClient();
			}
		}
		INSTANCE.loggers.clear();
	}
	
	/**
	 * Get IncidentLogger instance
	 * @return IncidentLogger instance 
	 */
	public static IncidentLogger get(){
		assert(INSTANCE.configurator != null);
		return INSTANCE;
	}
	
	/**
	 * Make new logger in your thread using your configurator as {@link IConfigurator} 
	 * @return new logger as {@link ILogger}
	 */
	public ILogger makeLogger(){
		ILogger logger = configurator.makeLogger();
		client.addLogger(logger);
		loggers.add(logger);
		return logger;
	}
	
	public IData getServerData(){
		return server.getData();
	} 
}
