package com.lym.twogoods.message;
/**
 * 有关新消息的设置
 * @author yao
 *
 */
public class ChatSetting {
	/**判断是否在聊天界面，如果是当前聊天对象有新消息发来不用notification，直接刷新界面*/
	public static boolean isShow = false;
	/**暂存聊天对象*/
	public static String otherUserName = "";
}
