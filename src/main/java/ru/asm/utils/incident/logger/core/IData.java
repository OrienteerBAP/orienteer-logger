package ru.asm.utils.incident.logger.core;

/**
 * Data manager for main module.
 * Should be able to apply data from {@link ILoggerData} to self 
 * 
 * @param <T> type of your inner object, returned by method {@link ILoggerData.get}
 */
public interface IData<T>{
	/**
	 * Apply data to self
	 * @param data contains object implementable to self
	 */
	void applyLoggerData(ILoggerData<T> data);
	/**
	 * To return self as serialized object 
	 * @return self, or part of self, serialized by your serializer
	 */
	String get();
	/**
	 * Data applied to self. Should be support indexes or something that will allow you to transmit all the data again and again
	 * @param newData value, returned by other IData.get() object
	 */
	void apply(String newData);
}
