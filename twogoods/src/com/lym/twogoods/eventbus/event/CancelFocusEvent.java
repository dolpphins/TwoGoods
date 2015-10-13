package com.lym.twogoods.eventbus.event;


/**
 * <p>
 * 	取消关注成功事件,该事件对象包含了取消的商品的GUID.
 * </p>
 * 
 * @author 麦灿标
 * */
public class CancelFocusEvent {

	/** 取消关注的商品GUID */
	private String GUID;

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}
	
	
	
}
