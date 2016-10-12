package ru.asm.utils.incident.logger.core;

/**
 * 
 * 
 */
public interface IData<T>{
	void applyLoggerData(ILoggerData<T> data);
	String get();
	void apply(String newData);//only for other IData "get" output
}
