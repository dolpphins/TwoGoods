package com.lym.twogoods;
/**
 * 用于获取当前用户的消息
 * @author yao
 *
 */
public class CurrentUserManeger {
	
	//当前用户的贰货号
	private static String username = null; 
	
	/**
	 * 设置当前用户的贰货号
	 * @return
	 */
	public static void setCurrentUserName(String name)
	{
		username = name;
	}
	/**
	 * 获取当前用户的贰货号
	 * @return
	 */
	public static String getCurrentUserName()
	{
			return username;
	}

}
