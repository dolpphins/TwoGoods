package com.lym.twogoods.eventbus.event;

public class ExitChatEvent {
	
	private String content;
	public ExitChatEvent(String str)
	{
		content = str;
	}
	
	public String getContent()
	{
		return content;
	}
}
