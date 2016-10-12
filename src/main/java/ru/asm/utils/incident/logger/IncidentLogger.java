package ru.asm.utils.incident.logger;

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
	
	private IConfigurator configurator;
	private IClient client;
	private IServer server;
	
	public IncidentLogger(IConfigurator configurator) {
		this.configurator = configurator;

		client = new Client(configurator);
		server = new Server(configurator);
	}
	
	public ILogger makeLogger(){
		ILogger logger = configurator.makeLogger();
		client.addLogger(logger);
		return logger;
	}
	
	public IData<?> getServerData(){
		return server.getData();
	} 
}
