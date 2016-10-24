package org.orienteer.inclogger;

import org.apache.log4j.Logger;
import org.orienteer.inclogger.IncidentLogger;
import org.orienteer.inclogger.core.DefaultConfigurator;
import org.orienteer.inclogger.core.interfaces.ILogger;

import static org.junit.Assert.*; 
import org.junit.Test; 
/**
 * 
 */
public class AppTest 
{
	@Test
    public void testApp()
    {

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
        
        Logger l = Logger.getLogger("test");

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
		
        assert(IncidentLogger.get().getServerData().getData()!=null);
    }
}
