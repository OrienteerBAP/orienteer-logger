package ru.asm.utils.incident.logger;

import ru.asm.utils.incident.logger.core.DefaultConfigurator;
import ru.asm.utils.incident.logger.core.ILogger;
import ru.asm.utils.incident.logger.core.Main;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Main mainModule = new Main(new DefaultConfigurator());
        ILogger logger = mainModule.makeLogger();
        logger.incident("Example incident");
        logger.incident("Example incident");
        logger.incident("Example incident");
        logger.incident("Example incident");
        System.out.println(mainModule.getServerData().get());
    }
}
