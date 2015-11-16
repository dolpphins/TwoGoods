package com.lym.twogoods.settings;

import android.app.Notification;


/***
 * 新消息提醒设置
 * @author yao
 *
 */

public class NewMessageSetting {
	
	private static NewMessageSetting mNewMessageSetting = new NewMessageSetting();
	
	/**新消息的声音设置*/
	private static int notifictionSound = Notification.DEFAULT_SOUND; 
	/**新消息的震动设置*/
	private static int notifictionVibrate = Notification.DEFAULT_VIBRATE;
	
	
	/**
	 * <p>
	 * 	获取NewMessageSetting唯一实例对象
	 * </p>
	 * 
	 * @return 返回NewMessageSetting唯一实例对象
	 * */
	public NewMessageSetting getInstance()
	{
		return mNewMessageSetting;
	}


	public int getNotifictionSound() {
		return notifictionSound;
	}


	public void setNotifictionSound(int notifictionSound) {
		NewMessageSetting.notifictionSound = notifictionSound;
	}


	public int getNotifictionVibrate() {
		return notifictionVibrate;
	}


	public void setNotifictionVibrate(int notifictionVibrate) {
		NewMessageSetting.notifictionVibrate = notifictionVibrate;
	}
	
	
}
