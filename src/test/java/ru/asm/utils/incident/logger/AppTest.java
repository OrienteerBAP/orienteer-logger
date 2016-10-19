package ru.asm.utils.incident.logger;

import org.apache.log4j.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.asm.utils.incident.logger.core.DefaultConfigurator;
import ru.asm.utils.incident.logger.core.ILogger;

/**
 * 
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * 
     */
    public void testApp()
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
		

        System.out.println(IncidentLogger.get().getServerData().getData());
    }
}
