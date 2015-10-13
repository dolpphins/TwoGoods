package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;
import com.lym.twogoods.config.ChatConfiguration;

import cn.bmob.v3.BmobObject;

/**
 * <p>
 * 	一条聊天记录实体类,对应本地数据库的chat_detail表
 * </p>
 * 
 * @author 麦灿标
 * */
public class ChatDetailBean extends BmobObject{

	/** id 主键,自增 */
	@DatabaseField
	private int id;
	
	/** GUID */
	@DatabaseField
	private String GUID;
	
	/** 用户名,即贰货号 */
	@DatabaseField
	private String username;
	
	/** 对方用户名 */
	@DatabaseField
	private String other_username;
	
	/** 最后一条消息类型,默认为{@link ChatConfiguration#TYPE_MESSAGE_UNKNOWN} */
	@DatabaseField
	private int message_type = ChatConfiguration.TYPE_MESSAGE_UNKNOWN;
	
	/** 消息,如果是本文,那么为文本内容;如果为图片,那么为图片的url(本地),如果为语音,那么为语音的url(本地) */
	@DatabaseField
	private String message;
	
	/** 图片或语音的url(网络),如果该消息为文本则该字段为空 */
	@DatabaseField
	private String network_url;
	
	/** 标记是我发出的,还是我接收到的,默认为我接收到的{@link ChatConfiguration#DIRECTION_I_RECEIVE} */
	@DatabaseField
	private int direction = ChatConfiguration.DIRECTION_I_RECEIVE;
	
	/** 发送时间,格式:时间戳 */
	@DatabaseField 
	private long publish_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
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

	public int getMessage_type() {
		return message_type;
	}

	public void setMessage_type(int message_type) {
		this.message_type = message_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNetwork_url() {
		return network_url;
	}

	public void setNetwork_url(String network_url) {
		this.network_url = network_url;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public long getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(long publish_time) {
		this.publish_time = publish_time;
	}
	
	
	
	
}
