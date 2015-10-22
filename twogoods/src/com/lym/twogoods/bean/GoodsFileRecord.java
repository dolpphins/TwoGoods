package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;
import com.lym.twogoods.config.FileType;

import cn.bmob.v3.BmobObject;

/**
 * <p>
 * 	商品相关上传文件信息实体类
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsFileRecord extends BmobObject {

	/** id,主键自增 */
	@DatabaseField(generatedId = true)
	private int id;
	
	/** GUID,全局唯一 */
	@DatabaseField
	private String GUID;
	
	/** 该文件属于的商品的id */
	@DatabaseField
	private int good_id;
	
	/** 该文件属于的商品的GUID */
	@DatabaseField
	private String good_GUID;
	
	/** Bmob文件上次后的到的filename */
	@DatabaseField
	private String filename;
	
	/** 文件类型,默认为{@link FileType#FILE_TYPE_UNKNOWN} */
	@DatabaseField
	private int type = FileType.FILE_TYPE_UNKNOWN;

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

	public int getGood_id() {
		return good_id;
	}

	public void setGood_id(int good_id) {
		this.good_id = good_id;
	}

	public String getGood_GUID() {
		return good_GUID;
	}

	public void setGood_GUID(String good_GUID) {
		this.good_GUID = good_GUID;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
