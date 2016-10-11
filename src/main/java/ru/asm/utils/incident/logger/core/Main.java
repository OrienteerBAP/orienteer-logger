package ru.asm.utils.incident.logger.core;

public class Main {
	
	private IConfigurator configurator;
	private IClient client;
	private IServer server;
	
	public Main(IConfigurator configurator) {
		this.configurator = configurator;

		client = new Client(configurator);
		server = new Server(configurator);
	}
	
	public ILogger makeLogger(){
		ILogger logger = configurator.makeLogger();
		client.addLogger(logger);
		return logger;
	}
	
	public IData getServerData(){
		return server.getData();
	} 
}
