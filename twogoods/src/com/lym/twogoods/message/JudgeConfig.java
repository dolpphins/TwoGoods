package com.lym.twogoods.message;
/**
 * 这个类用来判断ChatActivity是由谁启动的
 * @author 尧俊锋
 *
 */
public class JudgeConfig {
	/**从商品详情处启动ChatActivity，值为20*/
	public static final int FRAM_GOODS = 20;
	/**从最近聊天列表处启动ChatActivity，值为21*/
	public static final int FRAM_MESSAGE_LIST = 21;
	/**从notifiction处启动ChatActivity，值为22*/
	public static final int FRAM_NOTIFICTION = 22;
}
