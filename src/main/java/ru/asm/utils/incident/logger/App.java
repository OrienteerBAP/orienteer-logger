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
	private static final Logger loggerr = Logger.getLogger(App.class);
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
            logger.incident(e);
		}
        
//        Logger l = Logger.getLogger("test");
        Logger l = loggerr;

        l.warn("first");
        try{
            l.warn("second");
            if (true){
                throw new Exception("exception2 body");
            }
            IncidentLogger.close();
        }catch (Exception e) {
			l.error(e.getMessage(),e);
		}

        l.warn("third shouldn't be printed");

        IncidentLogger.close();
        System.out.println(IncidentLogger.get().getServerData().getData());
		/*
        int counter=0;
        do{
        	try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//counter++;
            System.out.println(counter++);
        }while(true);
*/
    }
}
