package com.lym.twogoods.eventbus.event;

/**
 * 完成录音后调用
 * @author yao
 *
 */
public class FinishRecordEvent {
	
	
	
	String path;
	
	
	public FinishRecordEvent(String str)
	{
		path = str;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String str)
	{
		path = str;
	}
	
}
