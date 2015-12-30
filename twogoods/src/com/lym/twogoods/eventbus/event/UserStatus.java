package com.lym.twogoods.eventbus.event;

import com.lym.twogoods.bean.User;

/**
 * EventBus对象,用户状态改变EventBus事件类
 * 
 * @author 麦灿标
 *
 */
public class UserStatus {

	public static enum LoginStatus {
		LOGIN, EXIT
	}
	
	/**
	 * 用户状态,LOGIN表示登录,EXIT表示退出登录
	 */
	private LoginStatus status;
	
	/**
	 * 如果status为LOGIN,那么该字段保存的是登录的User对象,
	 * 如果status为EXIT,那么该字段保存的是退出登录的User对象.
	 */
	private User user;

	public UserStatus() {}
	
	public UserStatus(UserStatus.LoginStatus status, User user) {
		this.status = status;
		this.user = user;
	}
	
	public LoginStatus getStatus() {
		return status;
	}

	public void setStatus(LoginStatus status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
