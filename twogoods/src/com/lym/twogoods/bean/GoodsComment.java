package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * <p>
 * 	商品评论信息实体类,对应本地数据库的goods_comment表
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsComment {

	/** id 主键,自增 */
	@DatabaseField(generatedId = true)
	private int id;
	
	/** 评论父id,如果为-1说明为评论,为某一评论id说明是那条评论的回复,默认为-1 */
	@DatabaseField
	private int parent_id = -1;
	
	/** 评论的商品id */
	@DatabaseField
	private int good_id;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public int getGood_id() {
		return good_id;
	}

	public void setGood_id(int good_id) {
		this.good_id = good_id;
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

}
