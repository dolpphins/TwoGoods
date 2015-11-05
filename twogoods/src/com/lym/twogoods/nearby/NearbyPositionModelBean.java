package com.lym.twogoods.nearby;

/**
 * 选择位置的实体类
 * 
 * @author 龙宇文
 * 
 * */
public class NearbyPositionModelBean {

	private String name; // 显示的位置
	private String positionLetters; // 显示位置拼音的首字母

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPositionLetters() {
		return positionLetters;
	}

	public void setPositionLetters(String sortLetters) {
		this.positionLetters = sortLetters;
	}
}
