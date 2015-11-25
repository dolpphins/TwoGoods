package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;

import cn.bmob.v3.BmobObject;

/**
 * <p>
 * 	商品关注表,对应本地数据库中的goods_focus表
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsFocus extends BmobObject{

	/** id 主键,自增 */
	@DatabaseField
	private int id;
	
	/** 用户名,即贰货号 */
	@DatabaseField
	private String username;
	
	/** 关注的商品ObjectId */
	@DatabaseField
	private String goods_objectId;

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
	
	
	
}
