package com.lym.twogoods.bean;

import cn.bmob.v3.BmobObject;

/**
 * 举报实体类
 * 
 * @author 麦灿标
 *
 */
public class Report extends BmobObject{

	private int id;
	
	/** 举报者用户名 */
	private String username;
	
	/** 被举报的商品ObjectId */
	private String goods_objectId;
	
	/** 举报具体信息 */
	private String message;
	
	/** 举报时间 */
	private long time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGoods_objectId() {
		return goods_objectId;
	}

	public void setGoods_objectId(String goods_objectId) {
		this.goods_objectId = goods_objectId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
	
}
