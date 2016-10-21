package ru.asm.utils.incident.logger;

import java.util.ArrayList;
import java.util.List;

import ru.asm.utils.incident.logger.core.Client;
import ru.asm.utils.incident.logger.core.IClient;
import ru.asm.utils.incident.logger.core.IConfigurator;
import ru.asm.utils.incident.logger.core.IData;
import ru.asm.utils.incident.logger.core.ILogger;
import ru.asm.utils.incident.logger.core.IServer;
import ru.asm.utils.incident.logger.core.Server;

/**
 * Entry point for incident logger
 * 
 */
public class IncidentLogger {
	
	private static IncidentLogger INSTANCE = new IncidentLogger();
	
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
