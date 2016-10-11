package ru.asm.utils.incident.logger.core;

import java.util.List;

public interface ILoggerData{
	public void add(String data);//need to be extend
	public String get();
	public void clear();
}
