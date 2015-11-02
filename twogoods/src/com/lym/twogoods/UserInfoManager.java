package com.lym.twogoods;

import com.lym.twogoods.bean.User;

/**
 * <p>
 * 	当前用户相关管理类,该类的某些方法为线程安全的,具体请看具体方法的说明
 * </p>
 * <p>
 * 	单例模式
 * </p>
 *  
 * */
public class UserInfoManager {

	/** 唯一实例  */
	private final static UserInfoManager sUserInfoManager = new UserInfoManager();
	
	/** 当前用户信息 */
	private User mCurrentUser;
	
	private UserInfoManager(){}
	
	/**
	 * <p>
	 * 	获取UserInfoManager单一实例
	 * </p>
	 * 
	 * @return 返回UserInfoManager单一实例
	 * */
	public static UserInfoManager getInstance() {
		return sUserInfoManager;
	}

	/**
	 * 获取当前登录用户对象,如果没有登录则返回null.
	 * 
	 * @return 返回当前登录用户对象,如果没有登录则返回null.
	 * */
	public User getmCurrent() {
		return mCurrentUser;
	}

	/**
	 * 设置当前登录用户,该方法为线程安全的.
	 * 
	 * @param 当前登录用户User对象
	 * */
	public synchronized void setmCurrent(User mCurrent) {
		this.mCurrentUser = mCurrent;
	}
	
	/**
	 * 判断是否已登录,该方法为线程安全的
	 * 
	 * @return 已登录返回true,否则返回false
	 * */
	public synchronized boolean isLogining() {
		if(mCurrentUser == null) {
			return false;
		} else {
			return true;
		}
	}
}
