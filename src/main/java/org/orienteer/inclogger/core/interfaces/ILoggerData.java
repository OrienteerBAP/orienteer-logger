package org.orienteer.inclogger.core.interfaces;

/**
 * Data manager for single logger
 * Can store single object(final exception), but have support for multiple objects(additional data,messages,etc.)
 * 
 * @param <T> type of your inner object, returned by method {@link get}
 */
public interface ILoggerData<T>{
	/**
	 * Set name-value pair for single object 
	 */
	public ILoggerData<T> set(String name,String value);
	/**
	 * End current object
	 */
	public void end();
	/**
	 * Get all objects as something, what you need
	 */
	public T get();
	/**
	 * Clear data to initial state
	 */
	public void clear();
}
