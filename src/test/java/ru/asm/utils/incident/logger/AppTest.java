package ru.asm.utils.incident.logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.asm.utils.incident.logger.core.DefaultConfigurator;
import ru.asm.utils.incident.logger.core.ILogger;
import ru.asm.utils.incident.logger.core.Main;

/**
 * Unit test for simple App.
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
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Main mainModule = new Main(new DefaultConfigurator());
        ILogger logger = mainModule.makeLogger();
        logger.incident("Example incident");
        logger.incident("Example incident");
        logger.incident("Example incident");
        logger.incident("Example incident");
        System.out.println(mainModule.getServerData().get());
        assertTrue( true );
    }
}
