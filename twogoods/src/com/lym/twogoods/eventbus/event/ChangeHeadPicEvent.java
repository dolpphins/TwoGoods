package com.lym.twogoods.eventbus.event;

/**
 * <p>
 * 	改变头像成功事件,该事件包含了改变后头像的本地url
 * </p>
 * 
 * @author 麦灿标
 * */
public class ChangeHeadPicEvent {

	/** 改变后头像的本地url */
	private String url;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
