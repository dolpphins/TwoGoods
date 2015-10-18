package com.lym.twogoods.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * <p>
 * 	基础屏幕工具类,包括获取屏幕宽高等
 * </p>
 * 
 * @author 麦灿标
 * */
public class BaseScreen {

	
	/**
	 * 获取屏幕宽度
	 * 
	 * @param at 
	 * 
	 * @return 屏幕宽度
	 * */
	public static int getScreenWidth(Activity at) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		at.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * 
	 * @param at 
	 * 
	 * @return 屏幕高度
	 * */
	public static int getScreenHeight(Activity at) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		at.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}
	
	/**
	 * <p>
	 * 	获取屏幕详细信息
	 * </p>
	 * 
	 * @param at
	 * 
	 * @return 屏幕详细信息对象
	 * */
	public static DisplayMetrics getScreenInfo(Activity at) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		at.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}
}
