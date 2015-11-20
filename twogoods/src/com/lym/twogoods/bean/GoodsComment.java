package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;

import cn.bmob.v3.BmobObject;

/**
 * <p>
 * 	商品评论信息实体类,对应本地数据库的goods_comment表
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsComment extends BmobObject{

	/** id 主键,自增 */
	@DatabaseField(generatedId = true)
	private int id;
	
	/** 评论父objectId,不为null表示是回复信息 */
	@DatabaseField
	private String parent_objectId;
	
	/** 评论的商品ObjectId */
	@DatabaseField
	private String good_objectId;
	
	/** id对应的用户名,即贰货号 */
	@DatabaseField
	private String username;
	
	/** 评论(或者回复)者的用户名 */
	@DatabaseField
	private String comment_username;
	
	/** 评论(回复)时间,格式:时间戳 */
	@DatabaseField
	private long time;
	
	/** 内容,注意可包含表情 */
	@DatabaseField
	private String content;
	
	/** 头像网络url */
	@DatabaseField
	private String head_url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParent_objectId() {
		return parent_objectId;
	}

	public void setParent_objectId(String parent_objectId) {
		this.parent_objectId = parent_objectId;
	}

	public String getGood_objectId() {
		return good_objectId;
	}

	public void setGood_objectId(String good_objectId) {
		this.good_objectId = good_objectId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment_username() {
		return comment_username;
	}

	public void setComment_username(String comment_username) {
		this.comment_username = comment_username;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}
	
	

}
