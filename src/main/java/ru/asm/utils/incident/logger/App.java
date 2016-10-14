package ru.asm.utils.incident.logger;

import org.apache.log4j.Logger;

import ru.asm.utils.incident.logger.core.DefaultConfigurator;
import ru.asm.utils.incident.logger.core.ILogger;

/**
 * 
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "----------" );
        IncidentLogger.init(new DefaultConfigurator());

        ILogger logger = IncidentLogger.get().makeLogger();
        logger.message("Example incident1");

        try{
            logger.message("Example incident2");
            if (true){
                throw new Exception("exception body");
            }
            IncidentLogger.close();
        }catch (Exception e) {
            logger.incident(e.getMessage());
		}
        
        Logger l = Logger.getLogger("test");

        l.warn("first");
        try{
            l.warn("second");
            if (true){
                throw new Exception("exception2 body");
            }
            IncidentLogger.close();
        }catch (Exception e) {
			l.error(e.getMessage());
		}

        l.warn("third shouldn't be printed");

        IncidentLogger.close();
		

        System.out.println(IncidentLogger.get().getServerData().get());

    }
}
