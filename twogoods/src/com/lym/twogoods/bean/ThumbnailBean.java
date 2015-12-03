package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * 缩略图信息实体类
 * 
 * @author 麦灿标
 *
 */
public class ThumbnailBean {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(uniqueIndex = true,columnName = "bmobFileName")
	private String bmobFileName;
	
	@DatabaseField
	private String visitUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBmobFileName() {
		return bmobFileName;
	}

	public void setBmobFileName(String bmobFileName) {
		this.bmobFileName = bmobFileName;
	}

	public String getVisitUrl() {
		return visitUrl;
	}

	public void setVisitUrl(String visitUrl) {
		this.visitUrl = visitUrl;
	}
	
	public static String getDbBmobFileNameColumnName() {
		return "bmobFileName";
	}
}
