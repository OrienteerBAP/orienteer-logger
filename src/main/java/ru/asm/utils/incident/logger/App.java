package ru.asm.utils.incident.logger;

import ru.asm.utils.incident.logger.core.DefaultConfigurator;
import ru.asm.utils.incident.logger.core.ILogger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        IncidentLogger mainModule = new IncidentLogger(new DefaultConfigurator());
        ILogger logger = mainModule.makeLogger();
        logger.message("Example incident");
        logger.incident("Example incident");
        logger.incident("Example incident");
        logger.incident("Example incident");
        System.out.println(mainModule.getServerData().get());
    }
}
