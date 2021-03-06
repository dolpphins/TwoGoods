package com.lym.twogoods.bean;

import java.io.Serializable;

/**
 * 位置信息实体类
 * 
 * @author 麦灿标
 *
 */
public class Location implements Serializable{

	/** 位置描述 */
	private String description;
	
	/** 经度 */
	private String longitude;
	
	/** 纬度 */
	private String latitude;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{description:");
		sb.append(description);
		sb.append(",");
		sb.append("longitude:");
		sb.append(longitude);
		sb.append(",");
		sb.append("latitude:");
		sb.append(latitude);
		sb.append("}");
		return sb.toString();
	}
	
}
