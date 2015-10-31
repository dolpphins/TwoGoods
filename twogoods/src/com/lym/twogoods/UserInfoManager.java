package com.lym.twogoods;

import com.lym.twogoods.bean.User;

/**
 * <p>
 * 	当前用户相关管理类
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
	private User mCurrent;
	
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

	public User getmCurrent() {
		return mCurrent;
	}

	public void setmCurrent(User mCurrent) {
		this.mCurrent = mCurrent;
	}
	
	
}
