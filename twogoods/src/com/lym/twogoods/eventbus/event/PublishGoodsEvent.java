package com.lym.twogoods.eventbus.event;

import com.lym.twogoods.bean.Goods;

/**
 * <p>
 * 	发布商品成功事件,该事件包含发布的商品信息
 * </p>
 * 
 * @author 麦灿标
 * */
public class PublishGoodsEvent {

	/** 发布的商品信息 */
	private Goods data;

	
	public Goods getData() {
		return data;
	}

	public void setData(Goods data) {
		this.data = data;
	}
	
	
}
