package com.lym.twogoods.config;

/**
 * <p>
 * 	消息相关配置
 * </p>
 * 
 * */
public class ChatConfiguration {

	/**
	 * 消息类型
	 * */
	
	/** 消息类型:未知 */
	public final static int TYPE_MESSAGE_UNKNOWN = 0;
	
	/** 消息类型:文字 */
	public final static int TYPE_MESSAGE_TEXT = 1;
	
	/** 消息类型:图片 */
	public final static int TYPE_MESSAGE_PICTURE = 2;
	
	/** 消息类型:语音 */
	public final static int TYPE_MESSAGE_VOICE = 3;
	
	/** 消息类型:位置*/
	public final static int TYPE_MESSAGE_LOCATION = 4;
	
	
	/**
	 * 消息发送方向
	 * */
	
	/** 我接收到的 */
	public final static int DIRECTION_I_RECEIVE = 0;
	
	/** 我发出的 */
	public final static int DIRECTION_I_SEND = 1;
	
}
