package com.lym.twogoods.eventbus.event;

/**
 * <p>
 * 	改变头像成功事件类
 * </p>
 * 
 * @author 麦灿标
 * */
public class HeadPicChangedEvent {

	/** 改变头像后之前的头像url(网络) */
	private String preUrl;
	
	/** 改变头像后的头像url(网络) */
	private String newUrl;

	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}
	
	
	
}
