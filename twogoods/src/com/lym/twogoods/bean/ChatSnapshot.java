package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;
import com.lym.twogoods.config.ChatConfiguration;

/**
 * <p>
 *  消息聊天列表,对应本地数据的chat_snapshot表
 * </p>
 * 
 * @author 麦灿标
 * */
public class ChatSnapshot {

	/** id 主键,自增 */
	@DatabaseField
	private int id;
	
	/** 用户名,即贰货号 */
	@DatabaseField
	private String username; 
	
	/** 对方用户名 */
	@DatabaseField
	private String other_username;
	
	/** 最后一次收到信息的时间,格式:时间戳 */
	@DatabaseField
	private long last_time;
	
	/** 未读信息数,默认为0 */
	@DatabaseField
	private int unread_num = 0;
	
	/** 最后一条消息内容,类型为文字为文字内容,图片或语音为url */
	@DatabaseField
	private String last_message;
	
	/** 最后一条消息类型,默认为{@link ChatConfiguration#TYPE_MESSAGE_UNKNOWN} */
	@DatabaseField
	private int last_message_type = ChatConfiguration.TYPE_MESSAGE_UNKNOWN;

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

	public String getOther_username() {
		return other_username;
	}

	public void setOther_username(String other_username) {
		this.other_username = other_username;
	}

	public long getLast_time() {
		return last_time;
	}

	public void setLast_time(long last_time) {
		this.last_time = last_time;
	}

	public int getUnread_num() {
		return unread_num;
	}

	public void setUnread_num(int unread_num) {
		this.unread_num = unread_num;
	}

	public String getLast_message() {
		return last_message;
	}

	public void setLast_message(String last_message) {
		this.last_message = last_message;
	}

	public int getLast_message_type() {
		return last_message_type;
	}

	public void setLast_message_type(int last_message_type) {
		this.last_message_type = last_message_type;
	}
	
	
	
}
