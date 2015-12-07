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
	
	/**
	 * 判断当前版本是否更新
	 * 
	 * @param context 上下文
	 * @param oldVersionCode 旧的版本号
	 * @return 当前版本为更新版本返回true,否则返回false.
	 */
	public static boolean isUpdate(Context context, int oldVersionCode) {
		if(context == null) {
			return false;
		}
		if(getAppVersion(context) > oldVersionCode) {
			return true;
		} else {
			return false;
		}
	}
}
