package com.lym.twogoods.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * 与兼容相关的工具类
 * 
 * @author yao
 *
 * */
public class MethodCompat {
	
	/**
	 * 获取SDK版本号
	 * 
	 * @return 返回SDK版本号
	 * 
	 * @author mao
	 */
	public static int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}
	
	/**
	 * {@link View#setBackground(Drawable drawbale)}方法的兼容版本
	 * 
	 * @param drawbale
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackground(View v, Drawable drawbale) {
		if(getSDKVersion() < 16) {
			v.setBackgroundDrawable(drawbale);
		} else {
			v.setBackground(drawbale);
		}
	}
}
