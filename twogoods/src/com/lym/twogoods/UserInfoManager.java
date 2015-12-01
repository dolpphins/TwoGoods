package com.lym.twogoods;

import com.lym.twogoods.bean.Location;
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
	
	/** 锁 */
	private Object userLock = new Object();
	
	/** 当前位置 */
	private Location mCurrentLocation;
	
	/** 锁 */
	private Object locationLock = new Object();
	
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
	public void setmCurrent(User mCurrent) {
		synchronized (userLock) {
			this.mCurrentUser = mCurrent;
		}
	}
	
	/**
	 * 判断是否已登录,该方法为线程安全的
	 * 
	 * @return 已登录返回true,否则返回false
	 * */
	public boolean isLogining() {
		synchronized (userLock) {
			if(mCurrentUser == null) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * 设置当前位置信息，该方法是线程安全的
	 * 
	 * @param location
	 */
	public void setCurrentLocation(Location location) {
		synchronized (locationLock) {
			mCurrentLocation = location;
		}
	}
	
	/**
	 * 获取当前位置信息
	 * 
	 * @return 返回当前位置信息
	 */
	public Location getCurrentLocation() {
		return mCurrentLocation;
	}
	
}
