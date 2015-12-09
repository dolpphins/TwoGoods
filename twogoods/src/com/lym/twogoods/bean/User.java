package com.lym.twogoods.bean;

import java.io.Serializable;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;

import cn.bmob.v3.BmobObject;

/**
 * <p>
 * 用户实体类,对应本地数据库的user表
 * </p>
 * 
 * @author 麦灿标
 * */
public class User extends BmobObject implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5981733722386364265L;

	/** id 主键,自增 */
	@DatabaseField
	private int id;

	/** GUID,全局唯一 */
	@DatabaseField
	private String GUID;

	/** 用户名,及贰货号 */
	@DatabaseField
	private String username;

	/** 密码（MD5） */
	@DatabaseField
	private String password;

	/** 性别,非空 */
	@DatabaseField
	private String sex;

	/** 联系方式 */
	@DatabaseField
	private String phone;

	/** 店铺浏览数,默认为0 */
	@DatabaseField
	private int browse_num = 0;

	/** 贰货宣言,即简介 */
	@DatabaseField
	private String declaration;

	/** 该用户头像网络可访问url */
	@DatabaseField
	private String head_url;
	
	/** 头像Bmob上传后得到的filename */
	@DatabaseField
	private String head_filename;

	/** 发布的贰货数,默认为0 */
	@DatabaseField
	private int publish_num = 0;

	/** 关注的贰货数,默认为0 */
	@DatabaseField
	private int focus_num;

	public User() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getBrowse_num() {
		return browse_num;
	}

	public void setBrowse_num(int browse_num) {
		this.browse_num = browse_num;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public int getPublish_num() {
		return publish_num;
	}

	public void setPublish_num(int publish_num) {
		this.publish_num = publish_num;
	}

	public int getFocus_num() {
		return focus_num;
	}

	public void setFocus_num(int focus_num) {
		this.focus_num = focus_num;
	}

	public String getHead_filename() {
		return head_filename;
	}

	public void setHead_filename(String head_filename) {
		this.head_filename = head_filename;
	}

	
	
}
