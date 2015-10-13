package com.lym.twogoods.eventbus.event;


/**
 * <p>
 * 	改变我的贰货宣言成功事件,该类包含了旧的和新的贰货宣言内容
 * </p>
 * 
 * @author 麦灿标
 * */
public class ChangeMineDeclarationEvent {

	/** 旧的贰货宣言内容 */
	private String oldData;
	
	/** 新的贰货宣言内容 */
	private String newData;

	
	public String getOldData() {
		return oldData;
	}

	public void setOldData(String oldData) {
		this.oldData = oldData;
	}

	public String getNewData() {
		return newData;
	}

	public void setNewData(String newData) {
		this.newData = newData;
	}
	
	
}
