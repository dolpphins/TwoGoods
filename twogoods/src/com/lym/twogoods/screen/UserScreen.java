package com.lym.twogoods.screen;

import android.content.Context;

/**
 * 与用户相关的屏幕适配类
 * 
 * @author 麦灿标
 * */
public class UserScreen {

	/** 我的界面头像大小,单位:dip */
	private final static int MINE_HEAD_PICTURE_SIZE = 100;
	
	/**
	 * 获取我的界面头像大小(正方形)
	 * 
	 * @param context 上下文,不能为null.
	 * 
	 * @return 获取成功返回相应的头像边长(正方形),获取失败返回0.
	 * */
	public static int getMineHeadPictureSize(Context context) {
		if(context == null) {
			return 0;
		}
		return DisplayUtils.dp2px(context, MINE_HEAD_PICTURE_SIZE);
	}
}
