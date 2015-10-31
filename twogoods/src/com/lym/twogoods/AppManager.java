package com.lym.twogoods;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * <p>
 * 	App管理类
 * </p>
 * 
 * @author 麦灿标
 * */
public class AppManager {

	private final static String TAG = "AppManager";
	
	/**
	 * <p>
	 * 	获取配置清单指定的版本号
	 * </p>
	 * 
	 * @param context 上下文,不能为null.
	 * 
	 * @return 获取成功返回版本号,获取失败返回-1.
	 * */
	public static int getAppVersion(Context context) {
		if(context == null) {
			return -1;
		}
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;//<manifest>清单文件指定的版本号
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
