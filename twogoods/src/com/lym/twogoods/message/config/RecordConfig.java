package com.lym.twogoods.message.config;


/**
 * 这个类用来判断当前的录音是在聊天使用录音还是在发布商品时使用录音，从而进行存储
 * @author 尧俊锋
 *
 */
public class RecordConfig {
	
	/**是聊天的语音*/
	public final static int CHAT_RECORD = 1;
	/**是发布商品时语音*/
	public final static int GOOD_RECORD = 2;
}
