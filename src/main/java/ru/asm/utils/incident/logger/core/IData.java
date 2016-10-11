package ru.asm.utils.incident.logger.core;

public interface IData{
	void applyLoggerData(ILoggerData data);
	String get();
	void apply(String newData);//only for other IData "get" output
}
