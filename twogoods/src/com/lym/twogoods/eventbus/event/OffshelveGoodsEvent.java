package com.lym.twogoods.eventbus.event;


/**
 * <p>
 * 	商品下线成功事件,该事件对象包含下线的商品的GUID. 
 * </p>
 * 
 * @author 麦灿标
 * */
public class OffshelveGoodsEvent {

	/** 下线的商品的GUID */
	private String GUID;

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}
	
	
}
