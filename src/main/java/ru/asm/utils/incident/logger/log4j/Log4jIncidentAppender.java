package ru.asm.utils.incident.logger.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import ru.asm.utils.incident.logger.IncidentLogger;
import ru.asm.utils.incident.logger.core.ILogger;
/**
 * Appender for log4j logger
 * Contains one additional property {@link icnidentLevel} to make logging more flexible
 *
 */
public class Log4jIncidentAppender extends AppenderSkeleton{

	protected ILogger incidentLogger;
	
	/**
	 * Log level, required to flush data to server
	 */
	protected Priority icnidentLevel = Level.ERROR;
	
    @Override
    protected void append(LoggingEvent event) {
    	if (incidentLogger == null) incidentLogger = IncidentLogger.get().makeLogger();
    	if (event.getLevel().isGreaterOrEqual(icnidentLevel)){
    		Throwable e = event.getThrowableInformation().getThrowable();
    		if (e!=null){
            	incidentLogger.incident(e);
    		}else{
            	incidentLogger.incident(event.getRenderedMessage());
    		}
    	}else{
        	incidentLogger.message(event.getRenderedMessage());
    	}
    }

    public void close() {
    }

    public boolean requiresLayout() {
        return false;
    }
    
	public Priority getIcnidentLevel() {
		return icnidentLevel;
	}

	public void setIcnidentLevel(Priority icnidentLevel) {
		this.icnidentLevel = icnidentLevel;
	}
    

}
