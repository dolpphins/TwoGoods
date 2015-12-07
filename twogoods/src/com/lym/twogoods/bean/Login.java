package com.lym.twogoods.bean;


/**
 * 封装登录、注册信息实体类
 * 
 * @author 麦灿标
 *
 */
public class Login {

	/** 用户名 */
	private String username;
	
	/** 密码 */
	private String password;
	
	/** 手机号码 */
	private String phone;
	
	/** 验证码 */
	private int vertifyCode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getVertifyCode() {
		return vertifyCode;
	}

	public void setVertifyCode(int vertifyCode) {
		this.vertifyCode = vertifyCode;
	}
	
	
}
