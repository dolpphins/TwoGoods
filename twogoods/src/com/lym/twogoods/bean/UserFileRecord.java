package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;

import cn.bmob.v3.BmobObject;

/**
 * <p>
 * 	用户相关上传文件信息实体类
 * </p>
 * 
 * @author 麦灿标
 * */
public class UserFileRecord extends BmobObject{

	/** id,主键自增 */
	@DatabaseField(generatedId = true)
	private int id;
	
	/** GUID,全局唯一 */
	@DatabaseField
	private String GUID;
	
	/** 该文件属于的user的id */
	@DatabaseField
	private int user_id;
	
	/** 该文件属于的user的GUID */
	@DatabaseField
	private String user_GUID;
	
	/** Bmob文件上传后的到的filename */
	@DatabaseField
	private String filename;

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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_GUID() {
		return user_GUID;
	}

	public void setUser_GUID(String user_GUID) {
		this.user_GUID = user_GUID;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
