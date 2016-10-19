package ru.asm.utils.incident.logger.core;

/**
 * Data manager for main module.
 * Should be able to apply data from your {@link ILoggerData} to self 
 * 
 */
public interface IData{
	enum IDataFlag{
		NOTHING,
		SENDED,
		SENDED_SUCCESSFULLY,
	}
	
	/**
	 * Apply data to self
	 * @param data contains object implementable to self
	 */
	void applyLoggerData(ILoggerData<?> data);
	/**
	 * To return self as serialized object 
	 * @return self, or part of self, serialized by your serializer
	 */
	String getData();
	
	/**
	 * To return self as serialized object, with mark this part of self by flag
	 * @param flag mark tour elements by this flag. Or make something else, like deleting :)
	 * @return self, or part of self, serialized by your serializer
	 */
	String getData(IDataFlag flag);
	
	/**
	 * Mark elements with "before" flag to "now" flag. Or make something else, like deleting.
	 * @param before
	 * @param now
	 */
	void mark(IDataFlag before,IDataFlag now);
	
	/**
	 * Data applied to self. Should be support indexes or something that will allow you to transmit all the data again and again
	 * @param newData value, returned by other IData.get() object
	 */
	void applyData(String newData);
}
