package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public interface ILoggerData<T>{
	public void set(String name,String value);
	public void end();// end current transaction
	public T get();// get all data 
	public void clear();
}
